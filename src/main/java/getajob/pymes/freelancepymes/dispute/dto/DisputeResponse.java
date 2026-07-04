package getajob.pymes.freelancepymes.dispute.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisputeResponse {
    private UUID disputeId;
    private UUID milestoneId;
    private String milestoneTitle;
    private String status;
    private Map<String, Object> aiResolutionReport;
    private boolean notificationsSent;
}
