package getajob.pymes.freelancepymes.dispute.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppealRequest {
    @NotBlank(message = "La razón de la apelación es requerida.")
    private String reason;
}
