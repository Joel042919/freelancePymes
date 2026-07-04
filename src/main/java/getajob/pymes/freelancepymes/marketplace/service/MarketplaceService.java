package getajob.pymes.freelancepymes.marketplace.service;

import getajob.pymes.freelancepymes.ai.entity.Assessment;
import getajob.pymes.freelancepymes.ai.repository.AssessmentRepository;
import getajob.pymes.freelancepymes.ai.service.AiService;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.marketplace.dto.ApplicationResponse;
import getajob.pymes.freelancepymes.marketplace.dto.ApplyRequest;
import getajob.pymes.freelancepymes.marketplace.dto.OfferResponse;
import getajob.pymes.freelancepymes.marketplace.dto.ReceivedApplicationResponse;
import getajob.pymes.freelancepymes.marketplace.dto.SkillGapResponse;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.marketplace.enums.OfferStatus;
import getajob.pymes.freelancepymes.marketplace.exception.SkillGapException;
import getajob.pymes.freelancepymes.marketplace.repository.ApplicationRepository;
import getajob.pymes.freelancepymes.marketplace.repository.JobOfferRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MarketplaceService {

    private final UserRepository userRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final JobOfferRepository jobOfferRepository;
    private final ApplicationRepository applicationRepository;
    private final AssessmentRepository assessmentRepository;
    private final AiService aiService;

    public List<OfferResponse> listOpenOffers() {
        return jobOfferRepository.findByStatus(OfferStatus.OPEN).stream()
                .map(this::toOfferResponse)
                .collect(Collectors.toList());
    }

    public OfferResponse getOffer(UUID offerId) {
        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("La oferta de trabajo no existe."));
        return toOfferResponse(offer);
    }

    private OfferResponse toOfferResponse(JobOffer offer) {
        PymeProfile pyme = offer.getPyme();
        return OfferResponse.builder()
                .id(offer.getId())
                .title(offer.getTitle())
                .description(offer.getDescription())
                .budgetType(offer.getBudgetType() != null ? offer.getBudgetType().name() : null)
                .totalBudget(offer.getTotalBudget())
                .status(offer.getStatus() != null ? offer.getStatus().name() : null)
                .pymeCompanyName(pyme != null ? pyme.getCompanyName() : null)
                .pymeVerified(pyme != null && Boolean.TRUE.equals(pyme.getVerificationBadge()))
                .requiredSkills(
                        offer.getRequiredSkills() == null
                                ? List.of()
                                : offer.getRequiredSkills().stream().map(Skill::getName).collect(Collectors.toList())
                )
                .build();
    }

    public List<ReceivedApplicationResponse> getReceivedApplications(String pymeEmail) {
        return applicationRepository.findByOffer_Pyme_User_Email(pymeEmail).stream()
                .map(application -> ReceivedApplicationResponse.builder()
                        .id(application.getId())
                        .offerId(application.getOffer().getId())
                        .offerTitle(application.getOffer().getTitle())
                        .freelancerName((application.getFreelancer().getFirstname() + " " + application.getFreelancer().getLastName()).trim())
                        .proposedAmount(application.getProposedAmount())
                        .estimatedDays(application.getEstimatedDays())
                        .status(application.getStatus().name())
                        .build())
                .collect(Collectors.toList());
    }

    public SkillGapResponse checkGap(UUID offerId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        FreelancerProfile freelancer = freelanceProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de freelancer."));

        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("La oferta de trabajo no existe."));

        Set<Skill> requiredSkills = offer.getRequiredSkills();

        List<Assessment> passedAssessments = assessmentRepository.findByFreelancerAndPassed(freelancer, true);
        Set<Skill> validatedSkills = passedAssessments.stream()
                .map(Assessment::getSkill)
                .collect(Collectors.toSet());

        List<Skill> missingSkills = requiredSkills.stream()
                .filter(skill -> !validatedSkills.contains(skill))
                .collect(Collectors.toList());

        boolean match100 = missingSkills.isEmpty();
        Map<String, Object> studyPlans = new HashMap<>();

        if (!match100) {
            for (Skill missingSkill : missingSkills) {
                studyPlans.put(missingSkill.getName(), aiService.generateStudyPlan(missingSkill.getName()));
            }
        }

        List<String> requiredNames = requiredSkills.stream().map(Skill::getName).collect(Collectors.toList());
        List<String> validatedNames = validatedSkills.stream().map(Skill::getName).collect(Collectors.toList());
        List<String> missingNames = missingSkills.stream().map(Skill::getName).collect(Collectors.toList());

        return SkillGapResponse.builder()
                .match100Percent(match100)
                .requiredSkills(requiredNames)
                .validatedSkills(validatedNames)
                .missingSkills(missingNames)
                .studyPlans(studyPlans)
                .build();
    }

    @Transactional
    public ApplicationResponse applyToOffer(UUID offerId, ApplyRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        FreelancerProfile freelancer = freelanceProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de freelancer."));

        JobOffer offer = jobOfferRepository.findById(offerId)
                .orElseThrow(() -> new IllegalArgumentException("La oferta de trabajo no existe."));

        if (applicationRepository.existsByOfferIdAndFreelancerId(offerId, freelancer.getId())) {
            throw new IllegalArgumentException("Ya has postulado a esta oferta de trabajo.");
        }

        Set<Skill> requiredSkills = offer.getRequiredSkills();

        List<Assessment> passedAssessments = assessmentRepository.findByFreelancerAndPassed(freelancer, true);
        Set<Skill> validatedSkills = passedAssessments.stream()
                .map(Assessment::getSkill)
                .collect(Collectors.toSet());

        List<Skill> missingSkills = requiredSkills.stream()
                .filter(skill -> !validatedSkills.contains(skill))
                .collect(Collectors.toList());

        if (!missingSkills.isEmpty()) {
            Map<String, Object> studyPlans = new HashMap<>();
            for (Skill missingSkill : missingSkills) {
                studyPlans.put(missingSkill.getName(), aiService.generateStudyPlan(missingSkill.getName()));
            }
            throw new SkillGapException(missingSkills, studyPlans);
        }

        Application application = new Application();
        application.setOffer(offer);
        application.setFreelancer(freelancer);
        application.setProposedAmount(request.getProposedAmount());
        application.setEstimatedDays(request.getEstimatedDays());
        application.setStatus(ApplicationStatus.PENDING);

        Application savedApplication = applicationRepository.save(application);

        return ApplicationResponse.builder()
                .id(savedApplication.getId())
                .offerId(offer.getId())
                .offerTitle(offer.getTitle())
                .freelancerId(freelancer.getId())
                .proposedAmount(savedApplication.getProposedAmount())
                .estimatedDays(savedApplication.getEstimatedDays())
                .status(savedApplication.getStatus().name())
                .build();
    }
}
