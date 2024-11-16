package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.RolPermisoDTO;
import GestionDocumental.entity.Permiso;
import GestionDocumental.entity.Rol;
import GestionDocumental.entity.RolPermisos;

@Component
public class RolPermisosMapper {
    public RolPermisoDTO toDto(RolPermisos rolPermisos) {
        RolPermisoDTO dto = new RolPermisoDTO();
        dto.setRolId(rolPermisos.getRol().getID());
        dto.setPermisoId(rolPermisos.getPermisos().getID());
        return dto;
    }

    public RolPermisos toEntity(RolPermisoDTO dto, Rol rol, Permiso permiso) {
        RolPermisos rolPermisos = new RolPermisos();
        rolPermisos.setRol(rol);
        rolPermisos.setPermisos(permiso);
        return rolPermisos;
    }
}
