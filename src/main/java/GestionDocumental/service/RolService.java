package GestionDocumental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Rol;
import GestionDocumental.repository.RolRepository;

@Service
public class RolService {

    private final RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    // Guardar un nuevo rol
    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

    // Obtener todos los roles
    public List<Rol> obtenerRoles() {
        return rolRepository.findAll();
    }

    // Obtener un rol por ID
    public Optional<Rol> obtenerRolPorId(int id) {
        return rolRepository.findById(id);
    }

    // Actualizar un rol
    public Rol actualizarRol(int id, String nombre, String descripcion) {
        Rol rol = obtenerRolPorId(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        rol.setNombre(nombre);
        rol.setDescripcion(descripcion);
        return rolRepository.save(rol);
    }

    // Eliminar un rol
    public void eliminarRol(int id) {
        rolRepository.deleteById(id);
    }
}
