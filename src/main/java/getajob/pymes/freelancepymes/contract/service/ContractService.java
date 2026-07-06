package getajob.pymes.freelancepymes.contract.service;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.contract.dto.ContractResponse;
import getajob.pymes.freelancepymes.contract.dto.MilestoneRequest;
import getajob.pymes.freelancepymes.contract.dto.MilestoneResponse;
import getajob.pymes.freelancepymes.contract.entity.Contract;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.contract.enums.ContractStatus;
import getajob.pymes.freelancepymes.contract.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.contract.repository.ContractRepository;
import getajob.pymes.freelancepymes.contract.repository.MilestoneRepository;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.marketplace.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final ContractRepository contractRepository;
    private final MilestoneRepository milestoneRepository;

    @Transactional
    public ContractResponse acceptApplication(UUID applicationId, List<MilestoneRequest> milestoneRequests, String pymeEmail) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new IllegalArgumentException("La postulación no existe."));

        User user = userRepository.findByEmail(pymeEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + pymeEmail));

        if (!application.getOffer().getPyme().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("No eres el dueño de la oferta asociada a esta postulación.");
        }

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalArgumentException("Solo se pueden aceptar postulaciones en estado PENDING. Estado actual: " + application.getStatus());
        }

        application.setStatus(ApplicationStatus.ACCEPTED);
        applicationRepository.save(application);

        Contract contract = new Contract();
        contract.setApplication(application);
        contract.setStatus(ContractStatus.DRAFT);
        Contract savedContract = contractRepository.save(contract);

        List<Milestone> milestones = new ArrayList<>();
        for (MilestoneRequest request : milestoneRequests) {
            Milestone milestone = new Milestone();
            milestone.setContract(savedContract);
            milestone.setTitle(request.getTitle());
            milestone.setAmount(request.getAmount());
            milestone.setDeadline(request.getDeadline());
            milestone.setStatus(MilestoneStatus.PENDING_FUNDING);
            milestones.add(milestoneRepository.save(milestone));
        }

        return toContractResponse(savedContract, milestones);
    }

    public List<ContractResponse> getMyContracts(String email) {
        Map<UUID, Contract> contractsById = new LinkedHashMap<>();
        contractRepository.findByApplication_Freelancer_User_Email(email).forEach(c -> contractsById.put(c.getId(), c));
        contractRepository.findByApplication_Offer_Pyme_User_Email(email).forEach(c -> contractsById.put(c.getId(), c));

        return contractsById.values().stream()
                .map(contract -> toContractResponse(contract, milestoneRepository.findByContract(contract)))
                .collect(Collectors.toList());
    }

    public ContractResponse getContract(UUID contractId, String email) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("El contrato no existe."));

        validateParticipant(contract, email);

        return toContractResponse(contract, milestoneRepository.findByContract(contract));
    }

    @Transactional
    public ContractResponse signContract(UUID contractId, String signatureImage, String email) {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("El contrato no existe."));

        boolean isFreelancer = contract.getApplication().getFreelancer().getUser().getEmail().equalsIgnoreCase(email);
        boolean isPyme = contract.getApplication().getOffer().getPyme().getUser().getEmail().equalsIgnoreCase(email);

        if (!isFreelancer && !isPyme) {
            throw new IllegalArgumentException("No eres parte de este contrato.");
        }

        if (contract.getStatus() != ContractStatus.DRAFT) {
            throw new IllegalArgumentException("El contrato ya no admite firmas. Estado actual: " + contract.getStatus());
        }

        if (isFreelancer) {
            contract.setDigitalSignatureFreelancer(signatureImage);
        } else {
            contract.setDigitalSignaturePyme(signatureImage);
        }

        if (contract.getDigitalSignatureFreelancer() != null && contract.getDigitalSignaturePyme() != null) {
            contract.setStatus(ContractStatus.SIGNED);
        }

        Contract saved = contractRepository.save(contract);
        return toContractResponse(saved, milestoneRepository.findByContract(saved));
    }

    private void validateParticipant(Contract contract, String email) {
        boolean isFreelancer = contract.getApplication().getFreelancer().getUser().getEmail().equalsIgnoreCase(email);
        boolean isPyme = contract.getApplication().getOffer().getPyme().getUser().getEmail().equalsIgnoreCase(email);
        if (!isFreelancer && !isPyme) {
            throw new IllegalArgumentException("No eres parte de este contrato.");
        }
    }

    private ContractResponse toContractResponse(Contract contract, List<Milestone> milestones) {
        Application application = contract.getApplication();
        var freelancer = application.getFreelancer();
        var pyme = application.getOffer().getPyme();

        return ContractResponse.builder()
                .id(contract.getId())
                .applicationId(application.getId())
                .offerTitle(application.getOffer().getTitle())
                .offerDescription(application.getOffer().getDescription())
                .freelancerName((freelancer.getFirstname() + " " + freelancer.getLastName()).trim())
                .pymeCompanyName(pyme.getCompanyName())
                .status(contract.getStatus().name())
                .signedByFreelancer(contract.getDigitalSignatureFreelancer() != null)
                .signedByPyme(contract.getDigitalSignaturePyme() != null)
                .milestones(milestones.stream().map(this::toMilestoneResponse).collect(Collectors.toList()))
                .build();
    }

    private MilestoneResponse toMilestoneResponse(Milestone milestone) {
        return MilestoneResponse.builder()
                .id(milestone.getId())
                .title(milestone.getTitle())
                .amount(milestone.getAmount())
                .deadline(milestone.getDeadline())
                .status(milestone.getStatus().name())
                .build();
    }
}
