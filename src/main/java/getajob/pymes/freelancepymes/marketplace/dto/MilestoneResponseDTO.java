package getajob.pymes.freelancepymes.marketplace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MilestoneResponseDTO {

    private UUID id;
    private String title;
    private String description;
    private Double amount;
    private String status;
    private LocalDate dueDate;
}
