package getajob.pymes.freelancepymes.profile.controller;

import getajob.pymes.freelancepymes.ai.service.AIEvaluationService;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.FreelancerSpecialty;
import getajob.pymes.freelancepymes.profile.entity.Silabo;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.FreelancerSpecialtyRepository;
import getajob.pymes.freelancepymes.profile.repository.SilaboRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/learning-paths")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LearningPathController {

    private final SilaboRepository silaboRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final FreelancerSpecialtyRepository freelancerSpecialtyRepository;
    private final AIEvaluationService aiEvaluationService;

    @GetMapping("/skills/{skillId}")
    public ResponseEntity<Map<String, Object>> getLearningPathForSkill(@PathVariable Integer skillId) {
        var skill = skillRepository.findById(skillId).orElse(null);
        if (skill == null) {
            return ResponseEntity.notFound().build();
        }

        List<Silabo> silabos = silaboRepository.findBySkillId(skillId);
        
        return ResponseEntity.ok(Map.of(
            "skillName", skill.getName(),
            "skillCategory", skill.getCategory() == null ? "General" : skill.getCategory(),
            "silabos", silabos
        ));
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<Map<String, Object>>> getRecommendedPaths(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        var user = userRepository.findByEmail(principal.getName()).orElse(null);
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        FreelancerProfile profile = freelanceProfileRepository.findById(user.getId()).orElse(null);
        if (profile == null) {
            return ResponseEntity.ok(List.of()); // No es freelancer
        }

        // Obtener especialidades a las que apuntan
        List<FreelancerSpecialty> specialties = freelancerSpecialtyRepository.findByFreelancerProfile_Id(profile.getId());
        Set<Skill> userSkills = profile.getSkills();

        List<Map<String, Object>> recommendedPaths = new ArrayList<>();
        int pathCounter = 1;

        for (FreelancerSpecialty fs : specialties) {
            Set<Skill> requiredSkills = fs.getSpecialty().getSkills();
            
            // Skill gap detection
            for (Skill reqSkill : requiredSkills) {
                if (!userSkills.contains(reqSkill)) {
                    List<Silabo> silabos = silaboRepository.findBySkillId(reqSkill.getId());
                    if (!silabos.isEmpty()) {
                        recommendedPaths.add(Map.of(
                            "id", "path-" + (pathCounter++),
                            "title", "Ruta de Aprendizaje: " + reqSkill.getName(),
                            "description", "Cubre la brecha detectada para alcanzar la especialidad de " + fs.getSpecialty().getName() + ".",
                            "progress", 0,
                            "modules", silabos
                        ));
                    }
                }
            }
        }

        return ResponseEntity.ok(recommendedPaths);
    }

    @GetMapping("/content/{silaboId}")
    public ResponseEntity<String> getInteractiveContent(@PathVariable Integer silaboId) {
        var silabo = silaboRepository.findById(silaboId).orElse(null);
        if (silabo == null) {
            return ResponseEntity.notFound().build();
        }

        String aiJson = aiEvaluationService.generateInteractiveContent(silabo.getPunto(), silabo.getDescription());
        return ResponseEntity.ok().header("Content-Type", "application/json").body(aiJson);
    }
}
