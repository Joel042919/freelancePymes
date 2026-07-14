package getajob.pymes.freelancepymes.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="submission_attachments")
@Getter @Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class SubmissionAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id", nullable = false)
    private EvaluationPhase phase;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_url", length = 512, nullable = false)
    private String fileUrl;

    @Column(name = "file_type", length = 50)
    private String fileType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
