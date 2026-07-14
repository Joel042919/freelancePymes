package getajob.pymes.freelancepymes.ai.entity;

import getajob.pymes.freelancepymes.ai.enums.EvaluationPhaseType;
import getajob.pymes.freelancepymes.profile.entity.Specialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="evaluation_phase_templates", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"specialty_id", "order_index"})
})
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class EvaluationPhaseTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id", nullable = false)
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    @Column(name = "phase_type", nullable = false)
    private EvaluationPhaseType phaseType;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(nullable = false)
    private String title;

    @Column(name = "instructions_prompt", columnDefinition = "TEXT", nullable = false)
    private String instructionsPrompt;

    @Column(name = "time_limit_minutes", nullable = false)
    private Integer timeLimitMinutes;

    @Column(name = "passing_score", nullable = false)
    private Double passingScore;
}
