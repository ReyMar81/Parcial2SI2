package GestionDocumental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Permiso;
import GestionDocumental.entity.RolPermisos;
import GestionDocumental.entity.Rol;
import GestionDocumental.repository.PermisoRepository;
import GestionDocumental.repository.RolPermisosRepository;
import GestionDocumental.repository.RolRepository;

@Service
public class RolPermisosService {

    private final RolPermisosRepository rolPermisosRepository;
    private final RolRepository rolesRepository;
    private final PermisoRepository permisosRepository;

    public RolPermisosService(RolPermisosRepository rolPermisosRepository, RolRepository rolesRepository,
            PermisoRepository permisosRepository) {
        this.rolPermisosRepository = rolPermisosRepository;
        this.rolesRepository = rolesRepository;
        this.permisosRepository = permisosRepository;
    }

    // Asigna un permiso a un rol
    public RolPermisos asignarPermisoARol(int rolId, int permisoId) {
        // Buscar el rol y el permiso en la base de datos
        Rol rol = rolesRepository.findById(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID " + rolId));
        Permiso permiso = permisosRepository.findById(permisoId)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con ID " + permisoId));

        // Crear la asignación de rol-permiso
        RolPermisos rolPermisos = new RolPermisos();
        rolPermisos.setRol(rol);
        rolPermisos.setPermisos(permiso);

        // Guardar la relación en la base de datos
        return rolPermisosRepository.save(rolPermisos);
    }

    // Obtener todos los permisos asignados a un rol
    public List<RolPermisos> obtenerPermisosPorRol(int rolId) {
        return rolPermisosRepository.findByRol_ID(rolId);
    }

    // Eliminar un permiso específico de un rol
    public void eliminarPermisoDeRol(int rolId, int permisoId) {
        Optional<RolPermisos> rolPermiso = rolPermisosRepository.findByRol_IDAndPermisos_ID(rolId, permisoId);
        rolPermiso.ifPresent(rolPermisosRepository::delete);
    }
}
