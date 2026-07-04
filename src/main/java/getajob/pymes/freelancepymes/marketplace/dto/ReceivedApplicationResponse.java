package getajob.pymes.freelancepymes.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceivedApplicationResponse {
    private UUID id;
    private UUID offerId;
    private String offerTitle;
    private String freelancerName;
    private Double proposedAmount;
    private Integer estimatedDays;
    private String status;
}
