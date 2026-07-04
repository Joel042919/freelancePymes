package getajob.pymes.freelancepymes.ai.service;

import getajob.pymes.freelancepymes.ai.dto.AssessmentResultResponse;
import getajob.pymes.freelancepymes.ai.dto.QuizQuestionResponse;
import getajob.pymes.freelancepymes.ai.dto.SkillStatusResponse;
import getajob.pymes.freelancepymes.ai.dto.StartAssessmentResponse;
import getajob.pymes.freelancepymes.ai.entity.Assessment;
import getajob.pymes.freelancepymes.ai.repository.AssessmentRepository;
import getajob.pymes.freelancepymes.auth.entity.User;
import getajob.pymes.freelancepymes.auth.repository.UserRepository;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.Skill;
import getajob.pymes.freelancepymes.profile.repository.FreelanceProfileRepository;
import getajob.pymes.freelancepymes.profile.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssessmentService {

    private final UserRepository userRepository;
    private final FreelanceProfileRepository freelanceProfileRepository;
    private final SkillRepository skillRepository;
    private final AssessmentRepository assessmentRepository;
    private final AiService aiService;

    public List<SkillStatusResponse> getMySkills(String email) {
        FreelancerProfile freelancer = resolveFreelancer(email);
        List<Assessment> assessments = assessmentRepository.findByFreelancer(freelancer);

        Map<Integer, Assessment> bestPassedBySkill = new HashMap<>();
        for (Assessment assessment : assessments) {
            if (Boolean.TRUE.equals(assessment.getPassed())) {
                bestPassedBySkill.merge(assessment.getSkill().getId(), assessment,
                        (a, b) -> a.getScore() >= b.getScore() ? a : b);
            }
        }

        Set<Skill> skills = freelancer.getSkills() == null ? Set.of() : freelancer.getSkills();
        return skills.stream()
                .map(skill -> {
                    Assessment passedAssessment = bestPassedBySkill.get(skill.getId());
                    return SkillStatusResponse.builder()
                            .skillId(skill.getId())
                            .skillName(skill.getName())
                            .category(skill.getCategory())
                            .alreadyPassed(passedAssessment != null)
                            .bestScore(passedAssessment != null ? passedAssessment.getScore() : null)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<SkillStatusResponse> getMyBadges(String email) {
        FreelancerProfile freelancer = resolveFreelancer(email);
        return assessmentRepository.findByFreelancerAndPassed(freelancer, true).stream()
                .map(assessment -> SkillStatusResponse.builder()
                        .skillId(assessment.getSkill().getId())
                        .skillName(assessment.getSkill().getName())
                        .category(assessment.getSkill().getCategory())
                        .alreadyPassed(true)
                        .bestScore(assessment.getScore())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public StartAssessmentResponse startAssessment(Integer skillId, String email) {
        FreelancerProfile freelancer = resolveFreelancer(email);
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("La habilidad no existe."));

        boolean declared = freelancer.getSkills() != null && freelancer.getSkills().contains(skill);
        if (!declared) {
            throw new IllegalArgumentException("Debes declarar esta habilidad en tu perfil antes de rendir la prueba.");
        }

        Map<String, Object> quizData = aiService.generateQuiz(skill.getName(), skill.getCategory());

        Assessment assessment = new Assessment();
        assessment.setFreelancer(freelancer);
        assessment.setSkill(skill);
        assessment.setQuizData(quizData);
        assessment.setScore(null);
        assessment.setPassed(null);
        Assessment saved = assessmentRepository.save(assessment);

        return StartAssessmentResponse.builder()
                .assessmentId(saved.getId())
                .skillName(skill.getName())
                .questions(toQuestionResponses(quizData))
                .build();
    }

    @Transactional
    public AssessmentResultResponse submitAssessment(UUID assessmentId, Map<Integer, Integer> answers, String email) {
        FreelancerProfile freelancer = resolveFreelancer(email);
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new IllegalArgumentException("La evaluación no existe."));

        if (!assessment.getFreelancer().getId().equals(freelancer.getId())) {
            throw new IllegalArgumentException("Esta evaluación no pertenece al usuario autenticado.");
        }

        Map<String, Object> result = aiService.gradeQuiz(assessment.getQuizData(), answers);
        Double score = (Double) result.get("score");
        Boolean passed = (Boolean) result.get("passed");

        assessment.setScore(score);
        assessment.setPassed(passed);
        assessmentRepository.save(assessment);

        return AssessmentResultResponse.builder()
                .assessmentId(assessment.getId())
                .skillName(assessment.getSkill().getName())
                .score(score)
                .passed(passed)
                .badgeAwarded(passed)
                .build();
    }

    @SuppressWarnings("unchecked")
    private List<QuizQuestionResponse> toQuestionResponses(Map<String, Object> quizData) {
        List<Map<String, Object>> questions = (List<Map<String, Object>>) quizData.get("questions");
        List<QuizQuestionResponse> result = new java.util.ArrayList<>();
        for (int i = 0; i < questions.size(); i++) {
            Map<String, Object> q = questions.get(i);
            result.add(QuizQuestionResponse.builder()
                    .index(i)
                    .question((String) q.get("question"))
                    .options((List<String>) q.get("options"))
                    .build());
        }
        return result;
    }

    private FreelancerProfile resolveFreelancer(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        return freelanceProfileRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("El usuario autenticado no tiene un perfil de freelancer."));
    }
}
