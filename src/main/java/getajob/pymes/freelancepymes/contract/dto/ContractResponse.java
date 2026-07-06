package getajob.pymes.freelancepymes.contract.dto;

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
public class ContractResponse {
    private UUID id;
    private UUID applicationId;
    private String offerTitle;
    private String offerDescription;
    private String freelancerName;
    private String pymeCompanyName;
    private String status;
    private boolean signedByFreelancer;
    private boolean signedByPyme;
    private List<MilestoneResponse> milestones;
}
