package getajob.pymes.freelancepymes.ai.entity;

import getajob.pymes.freelancepymes.ai.enums.FraudEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="fraud_telemetry_logs")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class FraudTelemetryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private EvaluationSession session;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false)
    private FraudEventType eventType;

    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(columnDefinition = "jsonb")
    private String metadata;
}
