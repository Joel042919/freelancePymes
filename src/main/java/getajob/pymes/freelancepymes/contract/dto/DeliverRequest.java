package getajob.pymes.freelancepymes.contract.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverRequest {
    @NotBlank(message = "El link de evidencia es requerido.")
    private String evidenceUrl;

    private String notes;
}
