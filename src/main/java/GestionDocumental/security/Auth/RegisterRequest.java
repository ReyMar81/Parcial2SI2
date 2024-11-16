package GestionDocumental.security.Auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "El campo usuario no puede estar vacío.")
    private String username;

    @NotEmpty(message = "El campo email no puede estar vacío.")
    private String email;

    @NotEmpty(message = "El campo contraseña no puede estar vacío.")
    private String password;

    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer rolesId;
    private Integer especialidadId;
}
