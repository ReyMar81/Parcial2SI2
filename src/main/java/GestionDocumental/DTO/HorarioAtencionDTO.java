package GestionDocumental.DTO;

import lombok.Data;

@Data
public class HorarioAtencionDTO {
    private int id;
    private String diaSemana;
    private String horaInicio;
    private String horaFin;
    private int cantidadFichas;
    private Integer idUsuario;
}
