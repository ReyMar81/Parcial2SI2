package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.CitaHoraDTO;
import GestionDocumental.entity.CitaHora;
import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.entity.Usuario;

@Component
public class CitaHoraMapper {
    public CitaHora toEntity(CitaHoraDTO dto, HorarioAtencion horarioAtencion) {
        CitaHora citaHora = new CitaHora();
        citaHora.setHoraInicio(dto.getHoraInicio());
        citaHora.setHoraFin(dto.getHoraFin());
        citaHora.setDisponibilidad(dto.getDisponibilidad());
        citaHora.setHorarioAtencion(horarioAtencion);
        return citaHora;
    }

    // Convertir de entidad a DTO
    public CitaHoraDTO toDto(CitaHora citaHora) {
        CitaHoraDTO dto = new CitaHoraDTO();
        dto.setId(citaHora.getID());
        dto.setHoraInicio(citaHora.getHoraInicio());
        dto.setHoraFin(citaHora.getHoraFin());
        dto.setDisponibilidad(citaHora.getDisponibilidad());
        dto.setIdHorarioAtencion(citaHora.getHorarioAtencion().getID());

        Usuario medico = citaHora.getHorarioAtencion().getHorarioatencionusuario();
        if (medico != null) {
            dto.setNombreDoctor(medico.getNombre() + " " + medico.getApellidoPaterno());
            dto.setIdUsuario(medico.getID());
            dto.setEspecialidad(medico.getUsuarioespecialidades().getNombre()); // Asigna la especialidad si aplica
        }

        return dto;
    }
}
