package getajob.pymes.freelancepymes.contract.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneRequest {
    @NotBlank(message = "El título del hito es requerido.")
    private String title;

    @NotNull(message = "El monto del hito es requerido.")
    @Min(value = 1, message = "El monto del hito debe ser mayor a 0.")
    private Double amount;

    @NotNull(message = "La fecha límite del hito es requerida.")
    @FutureOrPresent(message = "La fecha límite debe ser hoy o en el futuro.")
    private LocalDate deadline;
}
