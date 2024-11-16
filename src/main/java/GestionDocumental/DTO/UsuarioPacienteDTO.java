package GestionDocumental.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioPacienteDTO {
    private String userName;
    private String password;
    private String email;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private int rolId;
    private LocalDate fechaNacimiento;
    private int idAseguradora;
}
