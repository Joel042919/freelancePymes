package getajob.pymes.freelancepymes.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public abstract class RegisterRequest {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email address")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[A-Z]).*$", message = "Password must contain at least one uppercase letter")
    @Pattern(regexp = "^(?=.*[a-z]).*$", message = "Password must contain at least one lowercase letter")
    @Pattern(regexp = "^(?=.*[0-9]).*$", message = "Password must contain at least one number")
    @Pattern(regexp = "^(?=.*[!@#$%^&*()_+\\-=[\\]{};':\"\\\\|,.<>/?]).*$", message = "Password must contain at least one special character")
    @Pattern(regexp = ".{8,}$", message = "Password must be at least 8 characters long")
    private String password;

}
