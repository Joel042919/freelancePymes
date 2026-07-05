package getajob.pymes.freelancepymes.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferRequestDTO {

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 255, message = "El título no debe exceder los 255 caracteres")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotNull(message = "El tipo de presupuesto es obligatorio")
    private String budgetType;

    private String modality;

    private String projectCategory;

    private Integer estimatedDays;

    private Double totalBudget;

    private Double minBudget;

    private Double maxBudget;

    private String location;

    private Set<Integer> requiredSkillIds;

    private List<MilestoneRequestDTO> milestones;
}
