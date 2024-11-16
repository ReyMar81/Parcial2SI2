package GestionDocumental.service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.repository.HorarioAtencionRepository;

@Service
public class HorarioAtencionService {

    private final HorarioAtencionRepository horarioAtencionRepository;
    private final CitaHoraService citaHoraService;

    public HorarioAtencionService(HorarioAtencionRepository horarioAtencionRepository,
            CitaHoraService citaHoraService) {
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.citaHoraService = citaHoraService;
    }

    // Guardar un nuevo HorarioAtencion y generar CitaHora automáticamente
    public HorarioAtencion guardarHorario(HorarioAtencion horario) {
        // Guardar el HorarioAtencion en la base de datos
        HorarioAtencion nuevoHorario = horarioAtencionRepository.save(horario);

        // Llamar al CitaHoraService para generar las citas asociadas
        citaHoraService.createCitasByHorarioAtencion(nuevoHorario.getID());

        return nuevoHorario;
    }

    // Obtener todos los HorarioAtencion
    public List<HorarioAtencion> obtenerHorarios() {
        return horarioAtencionRepository.findAll();
    }

    // Obtener un HorarioAtencion por ID
    public Optional<HorarioAtencion> obtenerHorarioPorId(int id) {
        return horarioAtencionRepository.findById(id);
    }

    // Actualizar un HorarioAtencion
    public HorarioAtencion actualizarHorario(int id, String diaSemana, LocalTime horaInicio, LocalTime horaFin) {
        HorarioAtencion horario = obtenerHorarioPorId(id)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));
        horario.setDiaSemana(diaSemana);
        horario.setHoraInicio(horaInicio);
        horario.setHoraFin(horaFin);
        return horarioAtencionRepository.save(horario);
    }

    // Eliminar un HorarioAtencion
    public void eliminarHorario(int id) {
        horarioAtencionRepository.deleteById(id);
    }
}
