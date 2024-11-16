package GestionDocumental.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Aseguradora;
import GestionDocumental.repository.AseguradoraRepository;

@Service
public class AseguradoraService {

    private final AseguradoraRepository aseguradoraRepository;

    public AseguradoraService(AseguradoraRepository aseguradoraRepository) {
        this.aseguradoraRepository = aseguradoraRepository;
    }

    // Guardar una nueva aseguradora
    public Aseguradora guardarAseguradora(Aseguradora aseguradora) {
        return aseguradoraRepository.save(aseguradora);
    }

    // Obtener todas las aseguradoras
    public List<Aseguradora> obtenerAseguradoras() {
        return aseguradoraRepository.findAll();
    }

    // Obtener una aseguradora por ID
    public Optional<Aseguradora> obtenerAseguradoraPorId(int id) {
        return aseguradoraRepository.findById(id);
    }

    // Actualizar una aseguradora
    public Aseguradora actualizarAseguradora(int id, String nombre, String descripcion, String contacto) {
        Aseguradora aseguradora = obtenerAseguradoraPorId(id)
                .orElseThrow(() -> new RuntimeException("Aseguradora no encontrada con ID " + id));
        aseguradora.setNombre(nombre);
        aseguradora.setDescripcion(descripcion);
        aseguradora.setContacto(contacto);
        return aseguradoraRepository.save(aseguradora);
    }

    // Eliminar una aseguradora
    public void eliminarAseguradora(int id) {
        aseguradoraRepository.deleteById(id);
    }
}
