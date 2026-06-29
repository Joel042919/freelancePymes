package getajob.pymes.freelancepymes.marketplace.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyRequest {

    @NotNull(message = "El monto propuesto es requerido.")
    @Min(value = 1, message = "El monto propuesto debe ser mayor a cero.")
    private Double proposedAmount;

    @NotNull(message = "Los días estimados son requeridos.")
    @Min(value = 1, message = "Los días estimados deben ser al menos 1 día.")
    private Integer estimatedDays;
}
