package GestionDocumental.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import GestionDocumental.DTO.HorarioAtencionDTO;
import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.mapper.HorarioAtencionMapper;
import GestionDocumental.repository.UsuarioRepository;
import GestionDocumental.service.CitaHoraService;
import GestionDocumental.service.HorarioAtencionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/horarioAtencion")
public class HorarioAtencionController {

    private final HorarioAtencionService horarioAtencionService;
    private final HorarioAtencionMapper horarioAtencionMapper;
    private final UsuarioRepository usuarioRepository;
    private final CitaHoraService citaHoraService;

    public HorarioAtencionController(HorarioAtencionService horarioAtencionService,
            HorarioAtencionMapper horarioAtencionMapper, UsuarioRepository usuarioRepository,
            CitaHoraService citaHoraService) {
        this.horarioAtencionService = horarioAtencionService;
        this.horarioAtencionMapper = horarioAtencionMapper;
        this.usuarioRepository = usuarioRepository;
        this.citaHoraService = citaHoraService;
    }

    // Endpoint para crear un horario
    @PostMapping
    public ResponseEntity<HorarioAtencion> crearHorario(@RequestBody HorarioAtencionDTO horarioDTO) {
        HorarioAtencion horario = horarioAtencionMapper.toEntity(horarioDTO);
        HorarioAtencion nuevoHorario = horarioAtencionService.guardarHorario(horario);
        return new ResponseEntity<>(nuevoHorario, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los horarios
    @GetMapping
    public ResponseEntity<List<HorarioAtencionDTO>> obtenerTodosLosHorarios() {
        List<HorarioAtencionDTO> horarios = horarioAtencionService.obtenerHorarios()
                .stream()
                .map(horarioAtencionMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }

    // Endpoint para obtener un horario por ID
    @GetMapping("/{id}")
    public ResponseEntity<HorarioAtencionDTO> obtenerHorarioPorId(@PathVariable int id) {
        Optional<HorarioAtencion> horario = horarioAtencionService.obtenerHorarioPorId(id);
        return horario.map(value -> new ResponseEntity<>(horarioAtencionMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para actualizar un horario
    @PutMapping("/{id}")
    public ResponseEntity<HorarioAtencion> actualizarHorario(@PathVariable int id,
            @RequestBody HorarioAtencionDTO horarioDTO) {
        // Convertir el DTO en una entidad de HorarioAtencion
        HorarioAtencion horarioActualizado = horarioAtencionMapper.toEntity(horarioDTO);

        // Actualizar los detalles básicos del HorarioAtencion
        HorarioAtencion horario = horarioAtencionService.actualizarHorario(id, horarioActualizado.getDiaSemana(),
                horarioActualizado.getHoraInicio(), horarioActualizado.getHoraFin());

        // Actualizar cantidad de fichas y usuario asignado
        horario.setCantidadFichas(horarioDTO.getCantidadFichas());
        if (horarioDTO.getIdUsuario() != null) {
            usuarioRepository.findById(horarioDTO.getIdUsuario())
                    .ifPresent(usuario -> horario.setHorarioatencionusuario(usuario));
        }

        // Guardar el HorarioAtencion actualizado en la base de datos
        HorarioAtencion horarioGuardado = horarioAtencionService.guardarHorario(horario);

        // Llamar al servicio para eliminar las citas antiguas y crear nuevas citas
        citaHoraService.actualizarCitasPorHorarioAtencion(horarioGuardado.getID());

        return new ResponseEntity<>(horarioGuardado, HttpStatus.OK);
    }

    // Endpoint para eliminar un horario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHorario(@PathVariable int id) {
        horarioAtencionService.eliminarHorario(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
