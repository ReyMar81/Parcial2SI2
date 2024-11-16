package GestionDocumental.DTO;

import lombok.Data;

@Data
public class UsuarioMedicoUpdateDTO {
    private String userName;
    private String email;
    private String password;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
}
