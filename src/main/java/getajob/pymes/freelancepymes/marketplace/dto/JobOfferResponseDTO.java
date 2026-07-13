package getajob.pymes.freelancepymes.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferResponseDTO {

    private UUID id;
    private UUID pymeId;
    private String companyName;
    private String title;
    private String description;
    private String budgetType;
    private String modality;
    private String projectCategory;
    private Integer estimatedDays;
    private Double totalBudget;
    private Double minBudget;
    private Double maxBudget;
    private String location;
    private String status;
    private LocalDate publishedAt;
    private Set<String> requiredSkills;
    private List<MilestoneResponseDTO> milestones;
}
