package getajob.pymes.freelancepymes.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StartAssessmentResponse {
    private UUID assessmentId;
    private String skillName;
    private List<QuizQuestionResponse> questions;
}
