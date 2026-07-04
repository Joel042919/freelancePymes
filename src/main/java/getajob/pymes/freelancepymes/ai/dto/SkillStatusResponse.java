package getajob.pymes.freelancepymes.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkillStatusResponse {
    private Integer skillId;
    private String skillName;
    private String category;
    private Boolean alreadyPassed;
    private Double bestScore;
}
