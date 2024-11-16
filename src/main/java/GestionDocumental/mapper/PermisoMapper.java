package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.PermisoDTO;
import GestionDocumental.entity.Permiso;

@Component
public class PermisoMapper {
    public Permiso toEntity(PermisoDTO dto) {
        Permiso permiso = new Permiso();
        permiso.setNombre(dto.getNombre());
        permiso.setDescripcion(dto.getDescripcion());
        return permiso;
    }

    public PermisoDTO toDto(Permiso permiso) {
        PermisoDTO dto = new PermisoDTO();
        dto.setNombre(permiso.getNombre());
        dto.setDescripcion(permiso.getDescripcion());
        return dto;
    }
}
