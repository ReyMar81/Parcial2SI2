package GestionDocumental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import GestionDocumental.DTO.CitaHoraDTO;
import GestionDocumental.service.CitaHoraService;

@RestController
@RequestMapping("/citaHora")
public class CitaHoraController {
    private final CitaHoraService citaHoraService;

    public CitaHoraController(CitaHoraService citaHoraService) {
        this.citaHoraService = citaHoraService;
    }

    @GetMapping("/todas/semana")
    public ResponseEntity<Map<String, Object>> obtenerTodasLasCitasPorSemana() {
        Map<String, Object> todasLasCitasPorSemana = citaHoraService.obtenerTodasLasCitasPorSemana();
        return ResponseEntity.ok(todasLasCitasPorSemana);
    }

    // Obtener citas por semana, agrupadas por día
    @GetMapping("/semana")
    public ResponseEntity<Map<String, Object>> obtenerCitasPorSemana() {
        // Llama al método del servicio que devuelve la estructura organizada para todos
        // los médicos y especialidades
        Map<String, Object> citasPorSemana = citaHoraService.obtenerCitasPorSemana();
        return ResponseEntity.ok(citasPorSemana);
    }

    // Reservar una cita específica
    @PutMapping("/{id}/reservar")
    public CitaHoraDTO reservarCita(@PathVariable int id) {
        return citaHoraService.reservarCita(id);
    }
}
