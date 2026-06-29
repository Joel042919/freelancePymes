package getajob.pymes.freelancepymes.auth.service;

import getajob.pymes.freelancepymes.auth.dto.AuthResponse;
import getajob.pymes.freelancepymes.auth.dto.LoginRequest;
import getajob.pymes.freelancepymes.auth.dto.RegisterRequest;
import getajob.pymes.freelancepymes.auth.entity.Role;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;
import getajob.pymes.freelancepymes.auth.repository.RoleRepository;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.auth.security.JwtService;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private FreelanceProfileRepository freelanceProfileRepository;
    @Mock
    private PymeProfileRepository pymeProfileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private Role freelancerRole;
    private User testUser;

    @BeforeEach
    void setUp() {
        freelancerRole = new Role();
        freelancerRole.setId(1);
        freelancerRole.setName(RoleName.FREELANCER);

        testUser = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("hashed_password")
                .role(freelancerRole)
                .isActive(true)
                .build();
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("SecurePassword123!")
                .role("FREELANCER")
                .build();

        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(roleRepository.findByName(RoleName.FREELANCER)).thenReturn(Optional.of(freelancerRole));
        when(passwordEncoder.encode(any())).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(), any(), any())).thenReturn("mocked_jwt_token");

        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getToken());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("FREELANCER", response.getRole());

        verify(userRepository, times(1)).save(any(User.class));
        verify(freelanceProfileRepository, times(1)).save(any());
    }

    @Test
    void testRegisterDuplicateEmail() {
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("SecurePassword123!")
                .role("FREELANCER")
                .build();

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> authService.register(request));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLoginSuccess() {
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("SecurePassword123!")
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(any(), any(), any())).thenReturn("mocked_jwt_token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked_jwt_token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(any());
    }
}
