package getajob.pymes.freelancepymes.ai.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubmitAssessmentRequest {
    @NotNull(message = "Las respuestas son obligatorias.")
    private Map<Integer, Integer> answers;
}
