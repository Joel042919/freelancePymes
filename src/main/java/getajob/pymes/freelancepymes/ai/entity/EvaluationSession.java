package getajob.pymes.freelancepymes.ai.entity;

import getajob.pymes.freelancepymes.ai.enums.SessionStatus;
import getajob.pymes.freelancepymes.profile.entity.FreelancerProfile;
import getajob.pymes.freelancepymes.profile.entity.FreelancerSpecialty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="evaluation_sessions")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class EvaluationSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    private FreelancerProfile freelancerProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_specialty_id", nullable = false)
    private FreelancerSpecialty freelancerSpecialty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    @Column(name = "started_at")
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "final_score")
    private Double finalScore;

    @Column(name = "is_passed")
    private Boolean isPassed;
}
