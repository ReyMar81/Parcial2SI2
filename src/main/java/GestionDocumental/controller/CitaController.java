package GestionDocumental.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import GestionDocumental.DTO.CitaDTO;
import GestionDocumental.entity.Cita;
import GestionDocumental.entity.Usuario;
import GestionDocumental.mapper.CitaMapper;
import GestionDocumental.service.CitaScheduledService;
import GestionDocumental.service.CitaService;

@RestController
@RequestMapping("/citas")
public class CitaController {
    private final CitaService citaService;
    private final CitaMapper citaMapper;
    private final CitaScheduledService citaScheduledService;

    public CitaController(CitaService citaService, CitaMapper citaMapper, CitaScheduledService citaScheduledService) {
        this.citaService = citaService;
        this.citaMapper = citaMapper;
        this.citaScheduledService = citaScheduledService;
    }

    @PostMapping("/reservar")
    public ResponseEntity<CitaDTO> registrarCita(@RequestParam int citaHoraId) {
        // 1. Llamar al servicio para registrar la cita
        Cita nuevaCita = citaService.registrarCita(citaHoraId);

        // 2. Obtener los detalles de paciente y médico para construir el DTO
        Usuario paciente = nuevaCita.getCitausuario(); // Usuario que hizo la reserva
        Usuario medico = nuevaCita.getCitaHora().getHorarioAtencion().getHorarioatencionusuario(); // Médico asignado
                                                                                                   // desde CitaHora

        // 3. Convertir a DTO
        CitaDTO respuestaDTO = citaMapper.toDto(nuevaCita, paciente, medico);

        // 4. Retornar el DTO como respuesta
        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaDTO);
    }

    @PostMapping("/actualizar-estados")
    public ResponseEntity<Void> actualizarEstadosCitasManual() {
        citaScheduledService.actualizarEstadoCitas();
        return ResponseEntity.ok().build();
    }

}
