package getajob.pymes.freelancepymes.contract.service;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.contract.dto.ContractResponse;
import getajob.pymes.freelancepymes.contract.dto.MilestoneRequest;
import getajob.pymes.freelancepymes.contract.entity.Contract;
import getajob.pymes.freelancepymes.contract.entity.Milestone;
import getajob.pymes.freelancepymes.contract.enums.ContractStatus;
import getajob.pymes.freelancepymes.contract.enums.MilestoneStatus;
import getajob.pymes.freelancepymes.contract.repository.ContractRepository;
import getajob.pymes.freelancepymes.contract.repository.MilestoneRepository;
import getajob.pymes.freelancepymes.marketplace.entity.Application;
import getajob.pymes.freelancepymes.marketplace.entity.JobOffer;
import getajob.pymes.freelancepymes.marketplace.enums.ApplicationStatus;
import getajob.pymes.freelancepymes.marketplace.repository.ApplicationRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ContractRepository contractRepository;
    @Mock
    private MilestoneRepository milestoneRepository;

    @InjectMocks
    private ContractService contractService;

    private User pymeUser;
    private User freelancerUser;
    private PymeProfile pyme;
    private FreelancerProfile freelancer;
    private JobOffer offer;
    private Application application;

    @BeforeEach
    void setUp() {
        pymeUser = User.builder().id(UUID.randomUUID()).email("pyme@example.com").isActive(true).build();
        freelancerUser = User.builder().id(UUID.randomUUID()).email("freelancer@example.com").isActive(true).build();

        pyme = new PymeProfile();
        pyme.setId(pymeUser.getId());
        pyme.setUser(pymeUser);
        pyme.setCompanyName("Acme");

        freelancer = new FreelancerProfile();
        freelancer.setId(freelancerUser.getId());
        freelancer.setUser(freelancerUser);
        freelancer.setFirstname("John");
        freelancer.setLastName("Doe");

        offer = new JobOffer();
        offer.setId(UUID.randomUUID());
        offer.setTitle("Backend Position");
        offer.setDescription("Build the API");
        offer.setPyme(pyme);

        application = new Application();
        application.setId(UUID.randomUUID());
        application.setOffer(offer);
        application.setFreelancer(freelancer);
        application.setProposedAmount(1000.0);
        application.setEstimatedDays(10);
        application.setStatus(ApplicationStatus.PENDING);
    }

    @Test
    void testAcceptApplicationCreatesDraftContractWithMilestones() {
        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        when(userRepository.findByEmail(pymeUser.getEmail())).thenReturn(Optional.of(pymeUser));

        Contract savedContract = new Contract();
        savedContract.setId(UUID.randomUUID());
        savedContract.setApplication(application);
        savedContract.setStatus(ContractStatus.DRAFT);
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);

        Milestone savedMilestone = new Milestone();
        savedMilestone.setId(UUID.randomUUID());
        savedMilestone.setContract(savedContract);
        savedMilestone.setTitle("Diseño inicial");
        savedMilestone.setAmount(500.0);
        savedMilestone.setDeadline(LocalDate.now().plusDays(5));
        savedMilestone.setStatus(MilestoneStatus.PENDING_FUNDING);
        when(milestoneRepository.save(any(Milestone.class))).thenReturn(savedMilestone);

        MilestoneRequest request = new MilestoneRequest("Diseño inicial", 500.0, LocalDate.now().plusDays(5));

        ContractResponse response = contractService.acceptApplication(application.getId(), List.of(request), pymeUser.getEmail());

        assertNotNull(response);
        assertEquals(ContractStatus.DRAFT.name(), response.getStatus());
        assertEquals(1, response.getMilestones().size());
        assertEquals(ApplicationStatus.ACCEPTED, application.getStatus());
        verify(applicationRepository, times(1)).save(application);
    }

    @Test
    void testAcceptApplicationRejectsNonOwnerPyme() {
        User otherPyme = User.builder().id(UUID.randomUUID()).email("other@example.com").isActive(true).build();

        when(applicationRepository.findById(application.getId())).thenReturn(Optional.of(application));
        when(userRepository.findByEmail(otherPyme.getEmail())).thenReturn(Optional.of(otherPyme));

        MilestoneRequest request = new MilestoneRequest("Hito", 100.0, LocalDate.now().plusDays(1));

        assertThrows(IllegalArgumentException.class, () ->
                contractService.acceptApplication(application.getId(), List.of(request), otherPyme.getEmail())
        );

        verify(contractRepository, never()).save(any());
    }

    @Test
    void testSignContractBothPartiesResultsInSigned() {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setApplication(application);
        contract.setStatus(ContractStatus.DRAFT);
        contract.setDigitalSignatureFreelancer("freelancer-signature");

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));
        when(contractRepository.save(any(Contract.class))).thenAnswer(inv -> inv.getArgument(0));
        when(milestoneRepository.findByContract(contract)).thenReturn(List.of());

        ContractResponse response = contractService.signContract(contract.getId(), "pyme-signature", pymeUser.getEmail());

        assertEquals(ContractStatus.SIGNED.name(), response.getStatus());
        assertTrue(response.isSignedByFreelancer());
        assertTrue(response.isSignedByPyme());
    }

    @Test
    void testSignContractRejectsNonParticipant() {
        Contract contract = new Contract();
        contract.setId(UUID.randomUUID());
        contract.setApplication(application);
        contract.setStatus(ContractStatus.DRAFT);

        when(contractRepository.findById(contract.getId())).thenReturn(Optional.of(contract));

        assertThrows(IllegalArgumentException.class, () ->
                contractService.signContract(contract.getId(), "signature", "intruder@example.com")
        );

        verify(contractRepository, never()).save(any());
    }
}
