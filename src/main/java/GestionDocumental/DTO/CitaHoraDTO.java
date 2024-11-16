package GestionDocumental.DTO;

import java.time.LocalTime;

import lombok.Data;

@Data
public class CitaHoraDTO {
    private int id;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String disponibilidad;
    private int idHorarioAtencion;
    private String nombreDoctor;
    private int idUsuario;
    private String especialidad;
}
