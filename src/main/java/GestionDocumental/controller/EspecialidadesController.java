package GestionDocumental.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import GestionDocumental.DTO.EspecialidadesDTO;
import GestionDocumental.entity.Especialidades;
import GestionDocumental.mapper.EspecialidadesMapper;
import GestionDocumental.service.EspecialidadesService;

@RestController
@RequestMapping("/especialidades")

public class EspecialidadesController {
    private final EspecialidadesService especialidadesService;
    private final EspecialidadesMapper especialidadesMapper;

    public EspecialidadesController(EspecialidadesService especialidadesService,
            EspecialidadesMapper especialidadesMapper) {
        this.especialidadesService = especialidadesService;
        this.especialidadesMapper = especialidadesMapper;
    }

    // Endpoint para crear una especialidad
    @PostMapping
    public ResponseEntity<Especialidades> crearEspecialidad(@RequestBody EspecialidadesDTO especialidadDTO) {
        Especialidades especialidad = especialidadesMapper.toEntity(especialidadDTO);
        Especialidades nuevaEspecialidad = especialidadesService.guardarEspecialidad(especialidad);
        return new ResponseEntity<>(nuevaEspecialidad, HttpStatus.CREATED);
    }

    // Endpoint para obtener todas las especialidades
    @GetMapping
    public ResponseEntity<List<EspecialidadesDTO>> obtenerTodasLasEspecialidades() {
        List<EspecialidadesDTO> especialidades = especialidadesService.obtenerTodasLasEspecialidades()
                .stream()
                .map(especialidadesMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(especialidades, HttpStatus.OK);
    }

    // Endpoint para obtener una especialidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadesDTO> obtenerEspecialidadPorId(@PathVariable int id) {
        Optional<Especialidades> especialidad = especialidadesService.obtenerEspecialidadPorId(id);
        return especialidad.map(value -> new ResponseEntity<>(especialidadesMapper.toDto(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para actualizar una especialidad
    @PutMapping("/{id}")
    public ResponseEntity<Especialidades> actualizarEspecialidad(@PathVariable int id,
            @RequestBody EspecialidadesDTO especialidadDTO) {
        Especialidades especialidadActualizada = especialidadesMapper.toEntity(especialidadDTO);
        Especialidades especialidad = especialidadesService.actualizarEspecialidad(id,
                especialidadActualizada.getNombre(), especialidadActualizada.getDescripcion());
        return new ResponseEntity<>(especialidad, HttpStatus.OK);
    }

    // Endpoint para eliminar una especialidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable int id) {
        especialidadesService.eliminarEspecialidad(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
