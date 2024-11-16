package GestionDocumental.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CitaDTO {
    private int id;
    private LocalDate fechaCita;
    private LocalTime horaCita;
    private LocalDate fechaCreacion;
    private String estado;
    private int pacienteId; // ID del usuario del paciente
    private String nombrePaciente; // Nombre completo del paciente
    private int medicoId; // ID del usuario del médico
    private String nombreMedico; // Nombre completo del médico
    private int especialidadId; // ID de la especialidad del médico
    private String nombreEspecialidad; // Nombre de la especialidad
    private int citaHoraId; // ID de CitaHora
}
