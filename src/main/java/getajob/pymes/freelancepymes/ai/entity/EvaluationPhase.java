package getajob.pymes.freelancepymes.ai.entity;

import getajob.pymes.freelancepymes.ai.enums.EvaluationPhaseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="evaluation_phases")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class EvaluationPhase {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private EvaluationSession session;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private EvaluationPhaseTemplate template;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase_type", nullable = false)
    private EvaluationPhaseType phaseType;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "user_response_data", columnDefinition = "jsonb")
    private String userResponseData;

    @Column(name = "ai_feedback", columnDefinition = "jsonb")
    private String aiFeedback;

    @Column(name = "phase_score")
    private Double phaseScore;

    @Column(name = "time_spent_seconds")
    private Integer timeSpentSeconds;

    @Column(length = 20)
    private String status = "PENDING";

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "plagiarism_summary", columnDefinition = "jsonb")
    private String plagiarismSummary;
}
