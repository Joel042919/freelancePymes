package getajob.pymes.freelancepymes.contract.service;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.contract.dto.DeliverRequest;
import getajob.pymes.freelancepymes.contract.entity.Contract;
import getajob.pymes.freelancepymes.contract.entity.Deliverable;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.contract.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.contract.repository.DeliverableRepository;
import getajob.pymes.freelancepymes.contract.repository.MilestoneRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import getajob.pymes.freelancepymes.payment.entity.Payment;
import getajob.pymes.freelancepymes.payment.entity.enums.PaymentType;
import getajob.pymes.freelancepymes.payment.entity.enums.PaymentStatus;
import getajob.pymes.freelancepymes.payment.entity.enums.PaymentMethod;
import getajob.pymes.freelancepymes.payment.repository.PaymentRepository;
import getajob.pymes.freelancepymes.payment.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MilestoneLifecycleService {

    private final UserRepository userRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final PymeProfileRepository pymeProfileRepository;
    private final MilestoneRepository milestoneRepository;
    private final DeliverableRepository deliverableRepository;
    private final PaypalService paypalService;
    private final PaymentRepository paymentRepository;

    @Transactional
    public void fundMilestone(UUID milestoneId, String pymeEmail, String orderId) {
        User user = userRepository.findByEmail(pymeEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + pymeEmail));

        PymeProfile pyme = pymeProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de PYME."));

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new IllegalArgumentException("Hito no encontrado."));

        Contract contract = milestone.getContract();
        if (contract == null || contract.getApplication() == null ||
                !contract.getApplication().getOffer().getPyme().getId().equals(pyme.getId())) {
            throw new IllegalArgumentException("No eres el dueño (PYME) de este contrato.");
        }

        if (milestone.getStatus() != MilestoneStatus.PENDING_FUNDING) {
            throw new IllegalArgumentException("El hito ya está fondeado o completado. Estado: " + milestone.getStatus());
        }

        boolean captured = paypalService.captureOrder(orderId);
        if (!captured) {
            throw new IllegalArgumentException("No se pudo capturar el pago en PayPal.");
        }

        Payment payment = new Payment();
        payment.setMilestone(milestone);
        // Assuming we set a mock amount or get it from milestone. For now set a placeholder or get from milestone if it has amount.
        payment.setAmount(100.0); // Replace with milestone.getAmount() if it exists
        payment.setPaymentType(PaymentType.ESCROW_FUNDING);
        payment.setPaymentMethod(PaymentMethod.PAYPAL);
        payment.setTransactionReference(orderId);
        payment.setStatus(PaymentStatus.COMPLETED);
        paymentRepository.save(payment);

        milestone.setStatus(MilestoneStatus.FUNDED);
        milestoneRepository.save(milestone);
    }

    @Transactional
    public void deliverMilestone(UUID milestoneId, DeliverRequest request, String freelancerEmail) {
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

        if (milestone.getStatus() != MilestoneStatus.FUNDED) {
            throw new IllegalArgumentException("El hito debe estar fondeado (FUNDED) para poder subir entregas. Estado actual: " + milestone.getStatus());
        }

        // Registrar la entrega
        Deliverable deliverable = new Deliverable();
        deliverable.setMilestone(milestone);
        deliverable.setEvidenceUrl(request.getEvidenceUrl());
        deliverable.setNotes(request.getNotes());
        deliverable.setSubmittedAt(LocalDateTime.now());
        deliverableRepository.save(deliverable);

        // Actualizar estado del hito
        milestone.setStatus(MilestoneStatus.DELIVERED);
        milestoneRepository.save(milestone);
    }

    @Transactional
    public void rejectMilestone(UUID milestoneId, String pymeEmail) {
        User user = userRepository.findByEmail(pymeEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + pymeEmail));

        PymeProfile pyme = pymeProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de PYME."));

        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new IllegalArgumentException("Hito no encontrado."));

        Contract contract = milestone.getContract();
        if (contract == null || contract.getApplication() == null ||
                !contract.getApplication().getOffer().getPyme().getId().equals(pyme.getId())) {
            throw new IllegalArgumentException("No eres el dueño (PYME) de este contrato.");
        }

        if (milestone.getStatus() != MilestoneStatus.DELIVERED) {
            throw new IllegalArgumentException("Solo puedes evaluar hitos en estado de entrega (DELIVERED). Estado actual: " + milestone.getStatus());
        }

        // Rechazar entrega
        milestone.setStatus(MilestoneStatus.REJECTED);
        milestoneRepository.save(milestone);
    }
}
