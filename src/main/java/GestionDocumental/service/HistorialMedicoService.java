package GestionDocumental.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.HistorialMedico;
import GestionDocumental.entity.Pacientes;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.HistorialMedicoRepository;
import GestionDocumental.repository.PacientesRepository;

@Service
public class HistorialMedicoService {

    private final HistorialMedicoRepository historialMedicoRepository;
    private final PacientesRepository pacientesRepository;

    public HistorialMedicoService(HistorialMedicoRepository historialMedicoRepository,
            PacientesRepository pacientesRepository) {
        this.historialMedicoRepository = historialMedicoRepository;
        this.pacientesRepository = pacientesRepository;
    }

    // Método para crear el historial médico de un usuario con rol de paciente
    public void crearHistorialMedicoParaPaciente(Usuario usuario) {
        Pacientes paciente = pacientesRepository.findByPacienteusuario_ID(usuario.getID())
                .orElseThrow(
                        () -> new RuntimeException("Paciente no encontrado para el usuario con ID " + usuario.getID()));
        HistorialMedico historialMedico = new HistorialMedico();
        historialMedico.setFechaCreacion(LocalDate.now());
        historialMedico.setFechaActualizacion(LocalDate.now());
        historialMedico.setHistorialMedicoPaciente(paciente);
        historialMedicoRepository.save(historialMedico);
    }

    // Método para actualizar la fecha de actualización del historial médico
    public void actualizarFechaActualizacion(int historialMedicoId, LocalDate fechaSubida) {
        HistorialMedico historialMedico = historialMedicoRepository.findById(historialMedicoId)
                .orElseThrow(
                        () -> new RuntimeException("Historial médico no encontrado para el ID " + historialMedicoId));
        historialMedico.setFechaActualizacion(fechaSubida);
        historialMedicoRepository.save(historialMedico);
    }
}
