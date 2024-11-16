package GestionDocumental.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioAdminUpdateDTO {
    private String userName;
    private String email;
    private String password;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int rolId;
    private int especialidadId;
    private LocalDate fechaNacimiento;
    private int idAseguradora;
}
