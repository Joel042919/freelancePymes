package getajob.pymes.freelancepymes.profile.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileRequestDTO;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileResponseDTO;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class FreelancerProfileServiceTest {

    @Mock
    private FreelanceProfileRepository profileRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FreelancerProfileService profileService;

    private UUID userId;
    private User user;
    private FreelancerProfile profile;
    private Skill javaSkill;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);

        javaSkill = new Skill();
        javaSkill.setId(1);
        javaSkill.setName("Java");

        profile = new FreelancerProfile();
        profile.setId(userId);
        profile.setUser(user);
        profile.setFirstname("Juan");
        profile.setLastName("Perez");
        profile.setBio("Desarrollador full stack");
        profile.setSkills(new HashSet<>(Collections.singletonList(javaSkill)));
    }

    @Test
    void getProfile_existingProfile_returnsProfile() {
        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));

        FreelancerProfileResponseDTO result = profileService.getProfile(userId);

        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getFirstName()).isEqualTo("Juan");
        assertThat(result.getSkills()).hasSize(1);
    }

    @Test
    void getProfile_nonExistingProfile_throwsException() {
        when(profileRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profileService.getProfile(userId));
    }

    @Test
    void getOrCreateProfile_existingProfile_returnsProfileWithoutSaving() {
        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));

        FreelancerProfileResponseDTO result = profileService.getOrCreateProfile(userId);

        assertThat(result.getId()).isEqualTo(userId);
        verify(profileRepository, never()).save(any());
    }

    @Test
    void getOrCreateProfile_nonExistingProfile_createsProfile() {
        when(profileRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileResponseDTO result = profileService.getOrCreateProfile(userId);

        assertThat(result.getId()).isEqualTo(userId);
        verify(profileRepository).save(any(FreelancerProfile.class));
    }

    @Test
    void updateProfile_withNewSkill_createsSkillAndUpdatesProfile() {
        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));
        when(skillRepository.findByName("Python")).thenReturn(Optional.empty());
        when(skillRepository.save(any(Skill.class))).thenAnswer(invocation -> {
            Skill s = invocation.getArgument(0);
            s.setId(2);
            return s;
        });
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileRequestDTO dto = FreelancerProfileRequestDTO.builder()
                .firstName("Juan Carlos")
                .lastName("Perez")
                .bio("Updated bio")
                .skillNames(Arrays.asList("Python"))
                .build();

        FreelancerProfileResponseDTO result = profileService.updateProfile(userId, dto);

        assertThat(result.getFirstName()).isEqualTo("Juan Carlos");
        assertThat(result.getBio()).isEqualTo("Updated bio");
        assertThat(result.getSkills()).hasSize(1);
        verify(skillRepository).save(any(Skill.class));
    }

    @Test
    void updateProfile_withExistingSkill_usesExistingSkill() {
        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));
        when(skillRepository.findByName("Java")).thenReturn(Optional.of(javaSkill));
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileRequestDTO dto = FreelancerProfileRequestDTO.builder()
                .firstName("Juan")
                .lastName("Perez")
                .bio("Bio")
                .skillNames(Arrays.asList("Java"))
                .build();

        FreelancerProfileResponseDTO result = profileService.updateProfile(userId, dto);

        assertThat(result.getSkills()).hasSize(1);
        verify(skillRepository, never()).save(any(Skill.class));
    }

    @Test
    void removeSkill_existingSkill_removesSkillFromProfile() {
        when(profileRepository.findById(userId)).thenReturn(Optional.of(profile));
        when(skillRepository.findById(1)).thenReturn(Optional.of(javaSkill));
        when(profileRepository.save(any(FreelancerProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FreelancerProfileResponseDTO result = profileService.removeSkill(userId, 1);

        assertThat(result.getSkills()).isEmpty();
    }
}
