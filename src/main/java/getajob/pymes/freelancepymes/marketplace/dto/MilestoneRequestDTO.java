package getajob.pymes.freelancepymes.marketplace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneRequestDTO {

    @NotBlank(message = "El título del hito es obligatorio")
    private String title;

    private String description;

    @NotNull(message = "El monto del hito es obligatorio")
    @Positive(message = "El monto del hito debe ser positivo")
    private Double amount;

    @NotNull(message = "La fecha límite del hito es obligatoria")
    private LocalDate dueDate;
}
