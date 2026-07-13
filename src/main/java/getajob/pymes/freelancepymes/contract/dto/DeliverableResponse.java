package getajob.pymes.freelancepymes.contract.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverableResponse {
    private UUID id;
    private UUID milestoneId;
    private String evidenceUrl;
    private String notes;
    private LocalDateTime submittedAt;
}
