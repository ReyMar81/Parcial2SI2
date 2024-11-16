package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.CitaDTO;
import GestionDocumental.entity.CitaHora;
import GestionDocumental.entity.Cita;
import GestionDocumental.entity.Usuario;

@Component
public class CitaMapper {

    public CitaDTO toDto(Cita cita, Usuario paciente, Usuario medico) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getID());
        dto.setFechaCita(cita.getFechaCita());
        dto.setHoraCita(cita.getHoraCita());
        dto.setFechaCreacion(cita.getFechaCreacion());
        dto.setEstado(cita.getEstado());

        // Datos del paciente
        dto.setPacienteId(paciente.getID());
        dto.setNombrePaciente(paciente.getNombre());

        // Datos del médico
        dto.setMedicoId(medico.getID());
        dto.setNombreMedico(medico.getNombre());

        // Datos de la especialidad y citaHora
        dto.setEspecialidadId(medico.getUsuarioespecialidades().getID());
        dto.setNombreEspecialidad(medico.getUsuarioespecialidades().getNombre());
        dto.setCitaHoraId(cita.getCitaHora().getID());

        return dto;
    }

    public Cita toEntity(CitaDTO dto, Usuario medico, CitaHora citaHora) {
        Cita cita = new Cita();
        cita.setFechaCita(dto.getFechaCita()); // Mapear fechaCita
        cita.setHoraCita(dto.getHoraCita()); // Mapear horaCita
        cita.setFechaCreacion(dto.getFechaCreacion());
        cita.setEstado(dto.getEstado());

        // Asignar relaciones
        cita.setCitausuario(medico);
        cita.setCitaHora(citaHora);

        return cita;
    }
}
