package getajob.pymes.freelancepymes.profile.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileRequestDTO;
import getajob.pymes.freelancepymes.profile.dto.FreelancerProfileResponseDTO;
import getajob.pymes.freelancepymes.profile.dto.SkillDTO;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FreelancerProfileService {


    private final FreelanceProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public FreelancerProfileResponseDTO getProfile(UUID userId) {
        FreelancerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de freelancer no encontrado"));
        return mapToResponseDTO(profile);
    }

    @Transactional
    public FreelancerProfileResponseDTO getOrCreateProfile(UUID userId) {
        FreelancerProfile profile = profileRepository.findById(userId)
                .orElseGet(() -> createProfile(userId));
        return mapToResponseDTO(profile);
    }

    @Transactional
    public FreelancerProfileResponseDTO updateProfile(UUID userId, FreelancerProfileRequestDTO dto) {
        FreelancerProfile profile = profileRepository.findById(userId)
                .orElseGet(() -> createProfile(userId));

        profile.setFirstname(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        profile.setBio(dto.getBio());

        if (dto.getSkillNames() != null) {
            Set<Skill> newSkills = dto.getSkillNames().stream()
                    .map(this::findOrCreateSkill)
                    .collect(Collectors.toSet());
            profile.getSkills().clear();
            profile.getSkills().addAll(newSkills);
        } else if (profile.getSkills() == null) {
            profile.setSkills(new HashSet<>());
        }

        profileRepository.save(profile);
        profileRepository.flush();

        FreelancerProfile updated = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error al recuperar perfil después de actualizar"));
        return mapToResponseDTO(updated);
    }

    @Transactional
    public FreelancerProfileResponseDTO addSkills(UUID userId, List<String> skillNames) {
        FreelancerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de freelancer no encontrado"));

        if (profile.getSkills() == null) {
            profile.setSkills(new HashSet<>());
        }

        for (String name : skillNames) {
            Skill skill = findOrCreateSkill(name);
            profile.getSkills().add(skill);
        }

        profileRepository.saveAndFlush(profile);

        FreelancerProfile updated = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error al recuperar perfil después de agregar skills"));
        return mapToResponseDTO(updated);
    }

    @Transactional(readOnly = true)
    public List<SkillDTO> getAllAvailableSkills() {
        return skillRepository.findAll().stream()
                .map(skill -> SkillDTO.builder()
                        .id(skill.getId())
                        .name(skill.getName())
                        .category(skill.getCategory())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public FreelancerProfileResponseDTO removeSkill(UUID userId, Integer skillId) {
        FreelancerProfile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Perfil de freelancer no encontrado"));

        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new RuntimeException("Skill no encontrada"));

        profile.getSkills().remove(skill);
        profileRepository.saveAndFlush(profile);

        FreelancerProfile updated = profileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error al recuperar perfil después de eliminar skill"));
        return mapToResponseDTO(updated);
    }

    private FreelancerProfile createProfile(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        FreelancerProfile profile = new FreelancerProfile();
        profile.setUser(user);
        profile.setId(user.getId());
        profile.setFirstname("");
        profile.setLastName("");
        profile.setBio("");
        profile.setSkills(new HashSet<>());
        log.info("Perfil creado para usuario: {}", userId);
        return profileRepository.save(profile);
    }

    private Skill findOrCreateSkill(String name) {
        String normalizedName = name.trim();
        return skillRepository.findByName(normalizedName)
                .orElseGet(() -> {
                    Skill newSkill = new Skill();
                    newSkill.setName(normalizedName);
                    log.info("Skill creada: {}", normalizedName);
                    return skillRepository.save(newSkill);
                });
    }

    private FreelancerProfileResponseDTO mapToResponseDTO(FreelancerProfile profile) {
        List<SkillDTO> skillDTOs = profile.getSkills() != null
                ? profile.getSkills().stream()
                        .map(skill -> SkillDTO.builder()
                                .id(skill.getId())
                                .name(skill.getName())
                                .category(skill.getCategory())
                                .build())
                        .collect(Collectors.toList())
                : List.of();

        return FreelancerProfileResponseDTO.builder()
                .id(profile.getId())
                .firstName(profile.getFirstname() != null ? profile.getFirstname() : "")
                .lastName(profile.getLastName() != null ? profile.getLastName() : "")
                .bio(profile.getBio() != null ? profile.getBio() : "")
                .skills(skillDTOs)
                .build();
    }
}
