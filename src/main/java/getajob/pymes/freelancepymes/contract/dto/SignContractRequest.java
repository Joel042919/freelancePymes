package getajob.pymes.freelancepymes.contract.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignContractRequest {
    @NotBlank(message = "La firma es requerida.")
    private String signatureImage;
}
