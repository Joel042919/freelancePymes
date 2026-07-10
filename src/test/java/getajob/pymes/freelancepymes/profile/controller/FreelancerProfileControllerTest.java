package getajob.pymes.freelancepymes.profile.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import getajob.pymes.freelancepymes.auth.entity.Role;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileRequestDTO;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileResponseDTO;
import getajob.pymes.freelancepymes.profile.dto.SkillDTO;
import getajob.pymes.freelancepymes.profile.service.FreelancerProfileService;

@WebMvcTest(FreelancerProfileController.class)
class FreelancerProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FreelancerProfileService profileService;

    @Autowired
    private ObjectMapper objectMapper;

    private User authenticatedUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        Role freelancerRole = new Role();
        freelancerRole.setName(RoleName.FREELANCER);

        authenticatedUser = User.builder()
                .id(userId)
                .email("freelancer@example.com")
                .role(freelancerRole)
                .build();
    }

    @Test
    void getProfile_withoutAuth_returnsUnauthorized() throws Exception {
        FreelancerProfileResponseDTO response = FreelancerProfileResponseDTO.builder()
                .id(userId)
                .firstName("Juan")
                .lastName("Perez")
                .bio("Developer")
                .skills(Collections.singletonList(SkillDTO.builder().id(1).name("Java").build()))
                .build();

        when(profileService.getOrCreateProfile(userId)).thenReturn(response);

        mockMvc.perform(get("/api/freelancers/perfil/{userId}", userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProfile_withAuth_returnsOk() throws Exception {
        FreelancerProfileResponseDTO response = FreelancerProfileResponseDTO.builder()
                .id(userId)
                .firstName("Juan")
                .lastName("Perez")
                .bio("Developer")
                .skills(Collections.singletonList(SkillDTO.builder().id(1).name("Java").build()))
                .build();

        when(profileService.getOrCreateProfile(userId)).thenReturn(response);

        mockMvc.perform(get("/api/freelancers/perfil/{userId}", userId)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.skills[0].name").value("Java"));
    }

    @Test
    void getProfile_forDifferentUser_returnsForbidden() throws Exception {
        UUID otherUserId = UUID.randomUUID();
        FreelancerProfileResponseDTO response = FreelancerProfileResponseDTO.builder()
                .id(otherUserId)
                .firstName("Otro")
                .lastName("Usuario")
                .bio("Developer")
                .skills(Collections.emptyList())
                .build();

        when(profileService.getOrCreateProfile(otherUserId)).thenReturn(response);

        mockMvc.perform(get("/api/freelancers/perfil/{userId}", otherUserId)
                        .with(user(authenticatedUser)))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateProfile_withAuth_returnsOk() throws Exception {
        FreelancerProfileRequestDTO request = FreelancerProfileRequestDTO.builder()
                .firstName("Juan")
                .lastName("Perez")
                .bio("Developer")
                .skillNames(Collections.singletonList("Java"))
                .build();

        FreelancerProfileResponseDTO response = FreelancerProfileResponseDTO.builder()
                .id(userId)
                .firstName("Juan")
                .lastName("Perez")
                .bio("Developer")
                .skills(Collections.singletonList(SkillDTO.builder().id(1).name("Java").build()))
                .build();

        when(profileService.updateProfile(any(UUID.class), any(FreelancerProfileRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/freelancers/perfil/{userId}", userId)
                        .with(user(authenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Juan"));
    }

    @Test
    void removeSkill_withAuth_returnsOk() throws Exception {
        FreelancerProfileResponseDTO response = FreelancerProfileResponseDTO.builder()
                .id(userId)
                .firstName("Juan")
                .lastName("Perez")
                .bio("Developer")
                .skills(Collections.emptyList())
                .build();

        when(profileService.removeSkill(userId, 1)).thenReturn(response);

        mockMvc.perform(delete("/api/freelancers/perfil/{userId}/skills/{skillId}", userId, 1)
                        .with(user(authenticatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills").isEmpty());
    }
}
