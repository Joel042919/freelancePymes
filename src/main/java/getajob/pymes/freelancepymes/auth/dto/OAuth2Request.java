package getajob.pymes.freelancepymes.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2Request {

    @NotBlank(message = "El proveedor es requerido.")
    @Pattern(
            regexp = "^(google|linkedin)$",
            message = "El proveedor debe ser google o linkedin."
    )
    private String provider;

    @NotBlank(message = "El token es requerido.")
    private String token;

    @Pattern(
            regexp = "^(FREELANCER|PYME)$",
            message = "El rol debe ser FREELANCER o PYME si se especifica."
    )
    private String role;
}
