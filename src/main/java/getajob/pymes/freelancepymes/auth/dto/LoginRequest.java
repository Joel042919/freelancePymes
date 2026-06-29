package getajob.pymes.freelancepymes.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El correo electrónico es requerido.")
    @Email(message = "El formato de correo electrónico no es válido.")
    private String email;

    @NotBlank(message = "La contraseña es requerida.")
    private String password;
}
