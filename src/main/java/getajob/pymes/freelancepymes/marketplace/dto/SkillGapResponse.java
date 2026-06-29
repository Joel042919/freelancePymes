package getajob.pymes.freelancepymes.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillGapResponse {
    private boolean match100Percent;
    private List<String> requiredSkills;
    private List<String> validatedSkills;
    private List<String> missingSkills;
    private Map<String, Object> studyPlans;
}
