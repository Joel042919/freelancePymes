package getajob.pymes.freelancepymes.dispute.service;

import getajob.pymes.freelancepymes.ai.service.AiService;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.contract.entity.Contract;
import getajob.pymes.freelancepymes.contract.entity.Deliverable;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.contract.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.contract.repository.DeliverableRepository;
import getajob.pymes.freelancepymes.contract.repository.MilestoneRepository;
import getajob.pymes.freelancepymes.dispute.dto.AppealRequest;
import getajob.pymes.freelancepymes.dispute.dto.DisputeResponse;
import getajob.pymes.freelancepymes.dispute.entity.Dispute;
import getajob.pymes.freelancepymes.dispute.enums.DisputeStatus;
import getajob.pymes.freelancepymes.dispute.repository.DisputeRepository;
import getajob.pymes.freelancepymes.notification.service.NotificationService;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DisputeService {

    private final UserRepository userRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final MilestoneRepository milestoneRepository;
    private final DeliverableRepository deliverableRepository;
    private final DisputeRepository disputeRepository;
    private final AiService aiService;
    private final NotificationService notificationService;

    @Transactional
    public DisputeResponse appealMilestone(UUID milestoneId, AppealRequest request, String freelancerEmail) {
        User user = userRepository.findByEmail(freelancerEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + freelancerEmail));

        FreelancerProfile freelancer = freelanceProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de freelancer."));

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new IllegalArgumentException("Hito no encontrado."));

        Contract contract = milestone.getContract();
        if (contract == null || contract.getApplication() == null ||
                !contract.getApplication().getFreelancer().getId().equals(freelancer.getId())) {
            throw new IllegalArgumentException("No eres el freelancer asignado a este contrato.");
        }

        if (milestone.getStatus() != MilestoneStatus.REJECTED) {
            throw new IllegalArgumentException("Solo puedes apelar un hito que haya sido rechazado por la PYME. Estado actual: " + milestone.getStatus());
        }

        // 1. Bloquear los fondos cambiando el estado a DISPUTED
        milestone.setStatus(MilestoneStatus.DISPUTED);
        milestoneRepository.save(milestone);

        // 2. Obtener la entrega (evidencia) más reciente
        Deliverable deliverable = deliverableRepository.findFirstByMilestoneOrderBySubmittedAtDesc(milestone)
                .orElseThrow(() -> new IllegalArgumentException("No hay evidencias registradas de entrega para este hito."));

        // 3. Compilar texto del contrato y evidencias para la IA
        String contractText = String.format("Contrato: %s\nDescripción de la Oferta: %s\nMonto total: $%.2f\nDías estimados: %d\nHito a evaluar: %s\nMonto del Hito: $%.2f",
                contract.getApplication().getOffer().getTitle(),
                contract.getApplication().getOffer().getDescription(),
                contract.getApplication().getProposedAmount(),
                contract.getApplication().getEstimatedDays(),
                milestone.getTitle(),
                milestone.getAmount()
        );

        String evidenceText = String.format("Notas de la entrega: %s\nURL de evidencias: %s\nMotivo de la apelación del Freelancer: %s",
                deliverable.getNotes(),
                deliverable.getEvidenceUrl(),
                request.getReason()
        );

        // 4. Invocar el microservicio de IA
        Map<String, Object> aiReport = aiService.resolveDispute(contractText, evidenceText);

        // 5. Registrar la Disputa con su informe
        Dispute dispute = new Dispute();
        dispute.setMilestone(milestone);
        dispute.setAiResolutionReport(aiReport);
        dispute.setStatus(DisputeStatus.RESOLVED);
        Dispute savedDispute = disputeRepository.save(dispute);

        // 6. Despachar notificaciones
        String freelancerEmailAddress = contract.getApplication().getFreelancer().getUser().getEmail();
        String pymeEmailAddress = contract.getApplication().getOffer().getPyme().getUser().getEmail();

        String subject = "Resolución de Disputa por Agente Inteligente - Hito: " + milestone.getTitle();
        notificationService.sendDisputeReport(freelancerEmailAddress, subject, aiReport);
        notificationService.sendDisputeReport(pymeEmailAddress, subject, aiReport);

        return DisputeResponse.builder()
                .disputeId(savedDispute.getId())
                .milestoneId(milestone.getId())
                .milestoneTitle(milestone.getTitle())
                .status(savedDispute.getStatus().name())
                .aiResolutionReport(aiReport)
                .notificationsSent(true)
                .build();
    }
}
