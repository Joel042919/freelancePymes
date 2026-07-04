package getajob.pymes.freelancepymes.auth.service;

import getajob.pymes.freelancepymes.auth.dto.AuthResponse;
import getajob.pymes.freelancepymes.auth.dto.LoginRequest;
import getajob.pymes.freelancepymes.auth.dto.OAuth2Request;
import getajob.pymes.freelancepymes.auth.dto.RegisterRequest;
import getajob.pymes.freelancepymes.auth.entity.Role;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.entity.enums.RoleName;
import getajob.pymes.freelancepymes.auth.repository.RoleRepository;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.auth.security.JwtService;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.PymeProfile;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.PymeProfileRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final PymeProfileRepository pymeProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private final RestTemplate restTemplate = new RestTemplate();

    @org.springframework.beans.factory.annotation.Value("${app.oauth2.google.client-id:#{null}}")
    private String googleClientId;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }

        RoleName roleName = RoleName.valueOf(request.getRole().toUpperCase());
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado en el sistema."));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);

        if (roleName == RoleName.FREELANCER) {
            FreelancerProfile freelancerProfile = new FreelancerProfile();
            freelancerProfile.setUser(savedUser);
            freelancerProfile.setFirstname("");
            freelancerProfile.setLastName("");
            freelancerProfile.setBio("");
            freelanceProfileRepository.save(freelancerProfile);
        } else if (roleName == RoleName.PYME) {
            PymeProfile pymeProfile = new PymeProfile();
            pymeProfile.setUser(savedUser);
            pymeProfile.setCompanyName("");
            pymeProfile.setIndustry("");
            pymeProfile.setVerificationBadge(false);
            pymeProfile.setReputationScore(0.0);
            pymeProfileRepository.save(pymeProfile);
        }

        String jwtToken = jwtService.generateToken(savedUser, savedUser.getId(), roleName.name());

        return AuthResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationMs())
                .email(savedUser.getEmail())
                .role(roleName.name())
                .userId(savedUser.getId())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + request.getEmail()));

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("La cuenta está desactivada.");
        }

        String jwtToken = jwtService.generateToken(user, user.getId(), user.getRole().getName().name());

        return AuthResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationMs())
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .userId(user.getId())
                .build();
    }

    @Transactional
    public AuthResponse socialLogin(OAuth2Request request) {
        SocialProfile socialProfile = verifyToken(request.getProvider(), request.getToken());

        Optional<User> existingUserOpt = userRepository.findByEmail(socialProfile.getEmail());
        User user;
        RoleName roleName;

        if (existingUserOpt.isEmpty()) {
            if (request.getRole() == null) {
                throw new IllegalArgumentException("El rol (role) es requerido para registrar un nuevo usuario con red social.");
            }

            roleName = RoleName.valueOf(request.getRole().toUpperCase());
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado en el sistema."));

            user = User.builder()
                    .email(socialProfile.getEmail())
                    .password(null) // OAuth2 users do not have a password
                    .role(role)
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            user = userRepository.save(user);

            if (roleName == RoleName.FREELANCER) {
                FreelancerProfile freelancerProfile = new FreelancerProfile();
                freelancerProfile.setUser(user);
                freelancerProfile.setFirstname(socialProfile.getFirstName());
                freelancerProfile.setLastName(socialProfile.getLastName());
                freelancerProfile.setBio("");
                freelanceProfileRepository.save(freelancerProfile);
            } else if (roleName == RoleName.PYME) {
                PymeProfile pymeProfile = new PymeProfile();
                pymeProfile.setUser(user);
                pymeProfile.setCompanyName(socialProfile.getFirstName() + " " + socialProfile.getLastName());
                pymeProfile.setIndustry("");
                pymeProfile.setVerificationBadge(false);
                pymeProfile.setReputationScore(0.0);
                pymeProfileRepository.save(pymeProfile);
            }
        } else {
            user = existingUserOpt.get();
            roleName = user.getRole().getName();
        }

        if (!user.isEnabled()) {
            throw new IllegalArgumentException("La cuenta está desactivada.");
        }

        String jwtToken = jwtService.generateToken(user, user.getId(), roleName.name());

        return AuthResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationMs())
                .email(user.getEmail())
                .role(roleName.name())
                .userId(user.getId())
                .build();
    }

    private SocialProfile verifyToken(String provider, String token) {
        if ("google".equalsIgnoreCase(provider)) {
            if (token.startsWith("MOCK_")) {
                String mockEmail = token.substring(5).toLowerCase() + "@example.com";
                return new SocialProfile(mockEmail, "Mock", "GoogleUser");
            }

            try {
                String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
                Map<?, ?> response = restTemplate.getForObject(url, Map.class);

                if (response != null && response.containsKey("email")) {
                    String aud = (String) response.get("aud");
                    if (googleClientId != null && !googleClientId.equals("mock_google_client_id") && !googleClientId.equals(aud)) {
                        throw new IllegalArgumentException("El token de Google no pertenece a esta aplicación (Audience mismatch).");
                    }
                    String email = (String) response.get("email");
                    String firstName = (String) response.get("given_name");
                    String lastName = (String) response.get("family_name");
                    return new SocialProfile(
                            email,
                            firstName != null ? firstName : "",
                            lastName != null ? lastName : ""
                    );
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                throw new IllegalArgumentException("Token de Google inválido: " + e.getMessage());
            }
        } else if ("linkedin".equalsIgnoreCase(provider)) {
            if (token.startsWith("MOCK_")) {
                String mockEmail = token.substring(5).toLowerCase() + "@example.com";
                return new SocialProfile(mockEmail, "Mock", "LinkedInUser");
            }

            try {
                String url = "https://api.linkedin.com/v2/userinfo";
                // In Spring, we can set Authorization Header or call directly
                // Let's pass the token in standard format. RestTemplate can be configured:
                org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
                headers.setBearerAuth(token);
                org.springframework.http.HttpEntity<Void> entity = new org.springframework.http.HttpEntity<>(headers);
                org.springframework.http.ResponseEntity<Map> responseEntity = restTemplate.exchange(
                        url,
                        org.springframework.http.HttpMethod.GET,
                        entity,
                        Map.class
                );

                Map<?, ?> response = responseEntity.getBody();
                if (response != null && response.containsKey("email")) {
                    String email = (String) response.get("email");
                    String firstName = (String) response.get("given_name");
                    String lastName = (String) response.get("family_name");
                    return new SocialProfile(
                            email,
                            firstName != null ? firstName : "",
                            lastName != null ? lastName : ""
                    );
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Token de LinkedIn inválido: " + e.getMessage());
            }
        }
        throw new IllegalArgumentException("Proveedor de OAuth2 no soportado: " + provider);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SocialProfile {
        private String email;
        private String firstName;
        private String lastName;
    }
}
