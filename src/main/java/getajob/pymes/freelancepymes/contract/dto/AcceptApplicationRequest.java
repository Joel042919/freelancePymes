package getajob.pymes.freelancepymes.contract.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptApplicationRequest {
    @NotEmpty(message = "Debes definir al menos un hito para el contrato.")
    @Valid
    private List<MilestoneRequest> milestones;
}
