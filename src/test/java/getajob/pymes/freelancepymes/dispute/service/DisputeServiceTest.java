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
import getajob.pymes.freelancepymes.dispute.repository.DisputeRepository;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.notification.service.NotificationService;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DisputeServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FreelanceProfileRepository freelanceProfileRepository;
    @Mock
    private MilestoneRepository milestoneRepository;
    @Mock
    private DeliverableRepository deliverableRepository;
    @Mock
    private DisputeRepository disputeRepository;
    @Mock
    private AiService aiService;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private DisputeService disputeService;

    private User freelancerUser;
    private FreelancerProfile freelancer;
    private User pymeUser;
    private PymeProfile pyme;
    private Milestone milestone;
    private Contract contract;
    private Deliverable deliverable;

    @BeforeEach
    void setUp() {
        freelancerUser = User.builder()
                .id(UUID.randomUUID())
                .email("freelancer@example.com")
                .isActive(true)
                .build();

        freelancer = new FreelancerProfile();
        freelancer.setId(freelancerUser.getId());
        freelancer.setUser(freelancerUser);

        pymeUser = User.builder()
                .id(UUID.randomUUID())
                .email("pyme@example.com")
                .isActive(true)
                .build();

        pyme = new PymeProfile();
        pyme.setId(pymeUser.getId());
        pyme.setUser(pymeUser);

        JobOffer offer = new JobOffer();
        offer.setPyme(pyme);
        offer.setTitle("Job Title");
        offer.setDescription("Job Description");

        Application application = new Application();
        application.setFreelancer(freelancer);
        application.setOffer(offer);
        application.setProposedAmount(1500.0);
        application.setEstimatedDays(10);

        contract = new Contract();
        contract.setApplication(application);

        milestone = new Milestone();
        milestone.setId(UUID.randomUUID());
        milestone.setContract(contract);
        milestone.setTitle("Milestone Title");
        milestone.setAmount(500.0);
        milestone.setStatus(MilestoneStatus.REJECTED);

        deliverable = new Deliverable();
        deliverable.setMilestone(milestone);
        deliverable.setEvidenceUrl("http://evidence");
        deliverable.setNotes("Evidence notes");
    }

    @Test
    void testAppealMilestoneSuccess() {
        when(userRepository.findByEmail("freelancer@example.com")).thenReturn(Optional.of(freelancerUser));
        when(freelanceProfileRepository.findById(freelancerUser.getId())).thenReturn(Optional.of(freelancer));
        when(milestoneRepository.findById(milestone.getId())).thenReturn(Optional.of(milestone));
        when(deliverableRepository.findFirstByMilestoneOrderBySubmittedAtDesc(milestone))
                .thenReturn(Optional.of(deliverable));

        Map<String, Object> aiReport = Map.of(
                "verdict", "FAVORABLE_TO_FREELANCER",
                "reasoning", "Impartial reasoning"
        );
        when(aiService.resolveDispute(anyString(), anyString())).thenReturn(aiReport);

        Dispute dispute = new Dispute();
        dispute.setId(UUID.randomUUID());
        dispute.setMilestone(milestone);
        dispute.setAiResolutionReport(aiReport);
        dispute.setStatus(getajob.pymes.freelancepymes.dispute.enums.DisputeStatus.RESOLVED);

        when(disputeRepository.save(any(Dispute.class))).thenReturn(dispute);

        AppealRequest request = AppealRequest.builder()
                .reason("Everything was built perfectly")
                .build();

        DisputeResponse response = disputeService.appealMilestone(milestone.getId(), request, "freelancer@example.com");

        assertNotNull(response);
        assertEquals(milestone.getId(), response.getMilestoneId());
        assertEquals("RESOLVED", response.getStatus());
        assertEquals(MilestoneStatus.DISPUTED, milestone.getStatus()); // Funds locked!
        assertTrue(response.isNotificationsSent());

        verify(milestoneRepository, times(1)).save(milestone);
        verify(disputeRepository, times(1)).save(any(Dispute.class));
        verify(notificationService, times(2)).sendDisputeReport(anyString(), anyString(), eq(aiReport));
    }

    @Test
    void testAppealMilestoneNotRejectedFails() {
        milestone.setStatus(MilestoneStatus.DELIVERED); // Not REJECTED!

        when(userRepository.findByEmail("freelancer@example.com")).thenReturn(Optional.of(freelancerUser));
        when(freelanceProfileRepository.findById(freelancerUser.getId())).thenReturn(Optional.of(freelancer));
        when(milestoneRepository.findById(milestone.getId())).thenReturn(Optional.of(milestone));

        AppealRequest request = AppealRequest.builder()
                .reason("Wait")
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                disputeService.appealMilestone(milestone.getId(), request, "freelancer@example.com")
        );

        assertTrue(exception.getMessage().contains("Solo puedes apelar un hito que haya sido rechazado"));
        verify(disputeRepository, never()).save(any());
    }
}
