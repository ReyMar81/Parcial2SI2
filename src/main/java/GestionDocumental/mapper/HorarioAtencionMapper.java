package GestionDocumental.mapper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import GestionDocumental.DTO.HorarioAtencionDTO;
import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.UsuarioRepository;

@Component
public class HorarioAtencionMapper {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public HorarioAtencion toEntity(HorarioAtencionDTO dto) {
        HorarioAtencion horario = new HorarioAtencion();
        horario.setID(dto.getId());
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(LocalTime.parse(dto.getHoraInicio(), TIME_FORMATTER));
        horario.setHoraFin(LocalTime.parse(dto.getHoraFin(), TIME_FORMATTER));
        horario.setCantidadFichas(dto.getCantidadFichas());

        // Buscar el usuario y asignarlo si existe
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(dto.getIdUsuario());
        if (usuarioOptional.isPresent()) {
            horario.setHorarioatencionusuario(usuarioOptional.get());
        }

        return horario;
    }

    // Convertir de entidad a DTO
    public HorarioAtencionDTO toDto(HorarioAtencion horario) {
        HorarioAtencionDTO dto = new HorarioAtencionDTO();
        dto.setId(horario.getID());
        dto.setDiaSemana(horario.getDiaSemana());
        dto.setHoraInicio(horario.getHoraInicio().format(TIME_FORMATTER));
        dto.setHoraFin(horario.getHoraFin().format(TIME_FORMATTER));
        dto.setCantidadFichas(horario.getCantidadFichas());
        if (horario.getHorarioatencionusuario() != null) {
            dto.setIdUsuario(horario.getHorarioatencionusuario().getID());
        } else {
            dto.setIdUsuario(null);
        }
        return dto;
    }
}
