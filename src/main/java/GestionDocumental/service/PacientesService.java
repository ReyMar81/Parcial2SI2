package GestionDocumental.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import GestionDocumental.entity.Pacientes;
import GestionDocumental.repository.PacientesRepository;

@Service
public class PacientesService {

    private final PacientesRepository pacientesRepository;

    public PacientesService(PacientesRepository pacientesRepository) {
        this.pacientesRepository = pacientesRepository;
    }

    public Pacientes guardarPaciente(Pacientes paciente) {
        return pacientesRepository.save(paciente);
    }

    public List<Pacientes> obtenerPacientes() {
        return pacientesRepository.findAll();
    }

    public Optional<Pacientes> obtenerPacientePorId(int id) {
        return pacientesRepository.findById(id);
    }

    public Pacientes actualizarPaciente(int id, LocalDate fechaNacimiento) {
        Pacientes paciente = obtenerPacientePorId(id).orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        paciente.setFechaNacimiento(fechaNacimiento);
        return pacientesRepository.save(paciente);
    }

    public void eliminarPaciente(int id) {
        pacientesRepository.deleteById(id);
    }
}
