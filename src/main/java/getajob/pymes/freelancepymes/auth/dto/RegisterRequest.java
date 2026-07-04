package getajob.pymes.freelancepymes.auth.dto;

import jakarta.validation.constraints.Email;
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
public class RegisterRequest {

    @NotBlank(message = "El correo electrónico es requerido.")
    @Email(message = "El formato de correo electrónico no es válido.")
    private String email;

    @NotBlank(message = "La contraseña es requerida.")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe tener al menos 8 caracteres, una letra mayúscula, una letra minúscula, un número y un carácter especial (@$!%*?&)."
    )
    private String password;

    @NotBlank(message = "El rol es requerido.")
    @Pattern(
            regexp = "^(FREELANCER|PYME)$",
            message = "El rol debe ser FREELANCER o PYME."
    )
    private String role;

    private String firstName;

    private String lastName;
}
