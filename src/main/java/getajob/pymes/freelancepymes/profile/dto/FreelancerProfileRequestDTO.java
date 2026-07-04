package getajob.pymes.freelancepymes.profile.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FreelancerProfileRequestDTO {
    private String firstName;
    private String lastName;
    private String bio;
    private List<String> skillNames;
}
