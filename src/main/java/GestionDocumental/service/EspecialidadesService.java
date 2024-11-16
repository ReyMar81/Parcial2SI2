package GestionDocumental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Especialidades;
import GestionDocumental.repository.EspecialidadesRepository;

@Service
public class EspecialidadesService {

    private final EspecialidadesRepository especialidadesRepository;

    public EspecialidadesService(EspecialidadesRepository especialidadesRepository) {
        this.especialidadesRepository = especialidadesRepository;
    }

    // Guardar una especialidad
    public Especialidades guardarEspecialidad(Especialidades especialidad) {
        return especialidadesRepository.save(especialidad);
    }

    // Obtener todas las especialidades
    public List<Especialidades> obtenerTodasLasEspecialidades() {
        return especialidadesRepository.findAll();
    }

    // Obtener una especialidad por ID
    public Optional<Especialidades> obtenerEspecialidadPorId(int id) {
        return especialidadesRepository.findById(id);
    }

    // Actualizar una especialidad
    public Especialidades actualizarEspecialidad(int id, String nombre, String descripcion) {
        Especialidades especialidad = obtenerEspecialidadPorId(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        especialidad.setNombre(nombre);
        especialidad.setDescripcion(descripcion);
        return especialidadesRepository.save(especialidad);
    }

    // Eliminar una especialidad
    public void eliminarEspecialidad(int id) {
        especialidadesRepository.deleteById(id);
    }
}
