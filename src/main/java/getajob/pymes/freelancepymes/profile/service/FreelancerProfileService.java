package getajob.pymes.freelancepymes.profile.service;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FreelancerProfileService {

    private final FreelanceProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Transactional(readOnly = true)
    public FreelancerProfile getProfile(UUID userId) {
        return freelancerProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de freelancer no encontrado"));
    }

    @Transactional
    public FreelancerProfile updateProfile(UUID userId, String firstName, String lastName, String bio, List<Integer> skillIds) {
        FreelancerProfile profile = freelancerProfileRepository.findById(userId).orElse(null);
        
        if (profile == null) {
            // Si por alguna razón no existe (aunque debería crearse al registrarse), lo creamos
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            profile = new FreelancerProfile();
            profile.setUser(user);
            profile.setId(userId);
        }

        if (firstName != null) profile.setFirstname(firstName);
        if (lastName != null) profile.setLastName(lastName);
        if (bio != null) profile.setBio(bio);

        if (skillIds != null) {
            Set<Skill> newSkills = new HashSet<>(skillRepository.findAllById(skillIds));
            profile.setSkills(newSkills);
        }

        return freelancerProfileRepository.save(profile);
    }
}
