package GestionDocumental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Permiso;
import GestionDocumental.repository.PermisoRepository;

@Service
public class PermisoService {

    private final PermisoRepository permisoRepository;

    public PermisoService(PermisoRepository permisoRepository) {
        this.permisoRepository = permisoRepository;
    }

    // Guardar un nuevo permiso
    public Permiso guardarPermiso(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    // Obtener todos los permisos
    public List<Permiso> obtenerPermisos() {
        return permisoRepository.findAll();
    }

    // Obtener un permiso por ID
    public Optional<Permiso> obtenerPermisoPorId(int id) {
        return permisoRepository.findById(id);
    }

    // Actualizar un permiso
    public Permiso actualizarPermiso(int id, String nombre, String descripcion) {
        Permiso permiso = obtenerPermisoPorId(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permiso.setNombre(nombre);
        permiso.setDescripcion(descripcion);
        return permisoRepository.save(permiso);
    }

    // Eliminar un permiso
    public void eliminarPermiso(int id) {
        permisoRepository.deleteById(id);
    }
}
