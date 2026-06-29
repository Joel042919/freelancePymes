package getajob.pymes.freelancepymes.marketplace.service;

import getajob.pymes.freelancepymes.ai.entity.Assessment;
import getajob.pymes.freelancepymes.ai.repository.AssessmentRepository;
import getajob.pymes.freelancepymes.ai.service.AiService;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.marketplace.dto.ApplicationResponse;
import getajob.pymes.freelancepymes.marketplace.dto.ApplyRequest;
import getajob.pymes.freelancepymes.marketplace.dto.SkillGapResponse;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.marketplace.exception.SkillGapException;
import getajob.pymes.freelancepymes.marketplace.repository.ApplicationRepository;
import getajob.pymes.freelancepymes.marketplace.repository.JobOfferRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MarketplaceServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FreelanceProfileRepository freelanceProfileRepository;
    @Mock
    private JobOfferRepository jobOfferRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private AiService aiService;

    @InjectMocks
    private MarketplaceService marketplaceService;

    private User user;
    private FreelancerProfile freelancer;
    private JobOffer offer;
    private Skill java;
    private Skill react;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .email("freelancer@example.com")
                .isActive(true)
                .build();

        freelancer = new FreelancerProfile();
        freelancer.setId(user.getId());
        freelancer.setUser(user);

        java = new Skill();
        java.setId(1);
        java.setName("Java");

        react = new Skill();
        react.setId(2);
        react.setName("React");

        offer = new JobOffer();
        offer.setId(UUID.randomUUID());
        offer.setTitle("Backend Position");
        offer.setRequiredSkills(Set.of(java, react));
    }

    @Test
    void testCheckGap100PercentMatch() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(jobOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(offer));

        // Mock freelancer has passed assessments for both Java and React
        Assessment assessmentJava = new Assessment();
        assessmentJava.setSkill(java);
        assessmentJava.setPassed(true);

        Assessment assessmentReact = new Assessment();
        assessmentReact.setSkill(react);
        assessmentReact.setPassed(true);

        when(assessmentRepository.findByFreelancerAndPassed(freelancer, true))
                .thenReturn(List.of(assessmentJava, assessmentReact));

        SkillGapResponse response = marketplaceService.checkGap(offer.getId(), user.getEmail());

        assertNotNull(response);
        assertTrue(response.isMatch100Percent());
        assertTrue(response.getMissingSkills().isEmpty());
        assertTrue(response.getStudyPlans().isEmpty());
    }

    @Test
    void testCheckGapWithDeficit() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(jobOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(offer));

        // Mock freelancer only has Java (React is missing)
        Assessment assessmentJava = new Assessment();
        assessmentJava.setSkill(java);
        assessmentJava.setPassed(true);

        when(assessmentRepository.findByFreelancerAndPassed(freelancer, true))
                .thenReturn(List.of(assessmentJava));

        // Mock AI plan for React
        when(aiService.generateStudyPlan("React")).thenReturn(Map.of("plan", "Learn React in 4 weeks"));

        SkillGapResponse response = marketplaceService.checkGap(offer.getId(), user.getEmail());

        assertNotNull(response);
        assertFalse(response.isMatch100Percent());
        assertEquals(List.of("React"), response.getMissingSkills());
        assertTrue(response.getStudyPlans().containsKey("React"));
    }

    @Test
    void testApplyToOffer100PercentMatch() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(jobOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(offer));
        when(applicationRepository.existsByOfferIdAndFreelancerId(any(UUID.class), any(UUID.class))).thenReturn(false);

        Assessment assessmentJava = new Assessment();
        assessmentJava.setSkill(java);
        assessmentJava.setPassed(true);

        Assessment assessmentReact = new Assessment();
        assessmentReact.setSkill(react);
        assessmentReact.setPassed(true);

        when(assessmentRepository.findByFreelancerAndPassed(freelancer, true))
                .thenReturn(List.of(assessmentJava, assessmentReact));

        Application savedApp = new Application();
        savedApp.setId(UUID.randomUUID());
        savedApp.setOffer(offer);
        savedApp.setFreelancer(freelancer);
        savedApp.setProposedAmount(1500.0);
        savedApp.setEstimatedDays(15);
        savedApp.setStatus(ApplicationStatus.PENDING);

        when(applicationRepository.save(any(Application.class))).thenReturn(savedApp);

        ApplyRequest request = ApplyRequest.builder()
                .proposedAmount(1500.0)
                .estimatedDays(15)
                .build();

        ApplicationResponse response = marketplaceService.applyToOffer(offer.getId(), request, user.getEmail());

        assertNotNull(response);
        assertEquals(offer.getId(), response.getOfferId());
        assertEquals("PENDING", response.getStatus());
        verify(applicationRepository, times(1)).save(any(Application.class));
    }

    @Test
    void testApplyToOfferWithDeficit() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(jobOfferRepository.findById(any(UUID.class))).thenReturn(Optional.of(offer));
        when(applicationRepository.existsByOfferIdAndFreelancerId(any(UUID.class), any(UUID.class))).thenReturn(false);

        // Missing both Java and React
        when(assessmentRepository.findByFreelancerAndPassed(freelancer, true))
                .thenReturn(Collections.emptyList());

        when(aiService.generateStudyPlan(anyString())).thenReturn(Map.of("plan", "Mock learning path"));

        ApplyRequest request = ApplyRequest.builder()
                .proposedAmount(1500.0)
                .estimatedDays(15)
                .build();

        SkillGapException exception = assertThrows(SkillGapException.class, () ->
                marketplaceService.applyToOffer(offer.getId(), request, user.getEmail())
        );

        assertEquals("No cumples con todas las habilidades validadas requeridas para esta oferta.", exception.getMessage());
        assertEquals(2, exception.getMissingSkills().size());
        assertTrue(exception.getStudyPlans().containsKey("Java"));
        assertTrue(exception.getStudyPlans().containsKey("React"));
        verify(applicationRepository, never()).save(any());
    }
}
