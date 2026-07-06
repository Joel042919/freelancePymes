package getajob.pymes.freelancepymes.ai.service;

import getajob.pymes.freelancepymes.ai.dto.AssessmentResultResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FreelanceProfileRepository freelanceProfileRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private AssessmentRepository assessmentRepository;
    @Mock
    private AiService aiService;

    @InjectMocks
    private AssessmentService assessmentService;

    private User user;
    private FreelancerProfile freelancer;
    private Skill java;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(UUID.randomUUID())
                .email("freelancer@example.com")
                .isActive(true)
                .build();

        freelancer = new FreelancerProfile();
        freelancer.setId(user.getId());
        freelancer.setUser(user);

        java = new Skill();
        java.setId(1);
        java.setName("Java");
        java.setCategory("Backend");

        freelancer.setSkills(Set.of(java));
    }

    @Test
    void testGetMySkillsMarksPassedSkill() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));

        Assessment passed = new Assessment();
        passed.setFreelancer(freelancer);
        passed.setSkill(java);
        passed.setScore(90.0);
        passed.setPassed(true);

        when(assessmentRepository.findByFreelancer(freelancer)).thenReturn(List.of(passed));

        List<SkillStatusResponse> result = assessmentService.getMySkills(user.getEmail());

        assertEquals(1, result.size());
        assertTrue(result.get(0).getAlreadyPassed());
        assertEquals(90.0, result.get(0).getBestScore());
    }

    @Test
    void testStartAssessmentRejectsUndeclaredSkill() {
        Skill react = new Skill();
        react.setId(2);
        react.setName("React");
        react.setCategory("Frontend");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(2)).thenReturn(Optional.of(react));

        assertThrows(IllegalArgumentException.class, () ->
                assessmentService.startAssessment(2, user.getEmail())
        );

        verify(assessmentRepository, never()).save(any());
    }

    @Test
    void testStartAssessmentGeneratesQuizForDeclaredSkill() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1)).thenReturn(Optional.of(java));

        Map<String, Object> quiz = Map.of(
                "skill", "Java",
                "questions", List.of(
                        Map.of("question", "¿Qué es Java?", "options", List.of("A", "B"), "correctIndex", 0)
                )
        );
        when(aiService.generateQuiz("Java", "Backend")).thenReturn(quiz);

        Assessment saved = new Assessment();
        saved.setId(UUID.randomUUID());
        saved.setFreelancer(freelancer);
        saved.setSkill(java);
        saved.setQuizData(quiz);
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(saved);

        StartAssessmentResponse response = assessmentService.startAssessment(1, user.getEmail());

        assertNotNull(response);
        assertEquals("Java", response.getSkillName());
        assertEquals(1, response.getQuestions().size());
        assertEquals(0, response.getQuestions().get(0).getIndex());
        assertEquals("¿Qué es Java?", response.getQuestions().get(0).getQuestion());
    }

    @Test
    void testSubmitAssessmentUpdatesScoreAndPassed() {
        Assessment assessment = new Assessment();
        assessment.setId(UUID.randomUUID());
        assessment.setFreelancer(freelancer);
        assessment.setSkill(java);
        assessment.setQuizData(Map.of("questions", List.of()));

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(freelanceProfileRepository.findById(any(UUID.class))).thenReturn(Optional.of(freelancer));
        when(assessmentRepository.findById(assessment.getId())).thenReturn(Optional.of(assessment));
        when(aiService.gradeQuiz(any(), any())).thenReturn(Map.of(
                "score", 80.0,
                "passed", true,
                "correctAnswers", 4,
                "totalQuestions", 5
        ));

        AssessmentResultResponse result = assessmentService.submitAssessment(assessment.getId(), Map.of(0, 0), user.getEmail());

        assertEquals(80.0, result.getScore());
        assertTrue(result.getPassed());
        assertTrue(result.getBadgeAwarded());
        verify(assessmentRepository, times(1)).save(assessment);
    }
}
