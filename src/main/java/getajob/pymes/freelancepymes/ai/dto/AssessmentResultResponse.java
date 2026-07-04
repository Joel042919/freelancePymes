package getajob.pymes.freelancepymes.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentResultResponse {
    private UUID assessmentId;
    private String skillName;
    private Double score;
    private Boolean passed;
    private Boolean badgeAwarded;
}
