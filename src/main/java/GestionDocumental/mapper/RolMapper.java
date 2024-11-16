package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.RolDTO;
import GestionDocumental.entity.Rol;

@Component
public class RolMapper {
    public Rol toEntity(RolDTO dto) {
        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        return rol;
    }

    public RolDTO toDto(Rol rol) {
        RolDTO dto = new RolDTO();
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        return dto;
    }
}
