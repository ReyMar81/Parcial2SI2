package GestionDocumental.DTO;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ArchivoDTO {
    private int id;
    private String ruta;
    private String tipoDocumento;
    private LocalDate fechaSubida;
    private int idCita;
    private int idHistorialMedico;
}
