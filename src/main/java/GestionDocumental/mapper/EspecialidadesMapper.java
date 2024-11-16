package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.EspecialidadesDTO;
import GestionDocumental.entity.Especialidades;

@Component
public class EspecialidadesMapper {
    // Convertir de DTO a entidad
    public Especialidades toEntity(EspecialidadesDTO dto) {
        Especialidades especialidad = new Especialidades();
        especialidad.setID(dto.getId());
        especialidad.setNombre(dto.getNombre());
        especialidad.setDescripcion(dto.getDescripcion());
        return especialidad;
    }

    // Convertir de entidad a DTO
    public EspecialidadesDTO toDto(Especialidades especialidad) {
        EspecialidadesDTO dto = new EspecialidadesDTO();
        dto.setId(especialidad.getID());
        dto.setNombre(especialidad.getNombre());
        dto.setDescripcion(especialidad.getDescripcion());
        return dto;
    }
}
