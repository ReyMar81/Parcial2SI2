package GestionDocumental.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import GestionDocumental.entity.Cita;
import GestionDocumental.repository.CitaRepository;
import jakarta.transaction.Transactional;

@Service
public class CitaScheduledService {
    private final CitaRepository citaRepository;

    public CitaScheduledService(CitaRepository citaRepository) {
        this.citaRepository = citaRepository;
    }

    // Ejecuta cada minuto para actualizar el estado de las citas
    @Scheduled(fixedRate = 60000) // Cada minuto
    @Transactional
    public void actualizarEstadoCitas() {
        actualizarCitasEnProceso();
        actualizarCitasTerminadas();
    }

    private void actualizarCitasEnProceso() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Cita> citasPendientes = citaRepository.findByFechaCitaAndHoraCitaAndEstado(today, now, "Pendiente");
        for (Cita cita : citasPendientes) {
            cita.setEstado("En Proceso");
        }
        citaRepository.saveAll(citasPendientes);
    }

    private void actualizarCitasTerminadas() {
        LocalDate today = LocalDate.now();
        LocalTime now = LocalTime.now();

        List<Cita> citasEnProceso = citaRepository.findByFechaCitaAndEstado(today, "En Proceso");
        for (Cita cita : citasEnProceso) {
            LocalTime horaFin = cita.getCitaHora().getHoraFin();
            if (now.isAfter(horaFin)) {
                cita.setEstado("Terminado");
            }
        }
        citaRepository.saveAll(citasEnProceso);
    }
}
