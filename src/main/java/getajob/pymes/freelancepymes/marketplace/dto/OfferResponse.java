package getajob.pymes.freelancepymes.marketplace.dto;

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
public class OfferResponse {
    private UUID id;
    private String title;
    private String description;
    private String budgetType;
    private Double totalBudget;
    private String status;
    private String pymeCompanyName;
    private Boolean pymeVerified;
    private List<String> requiredSkills;
}
