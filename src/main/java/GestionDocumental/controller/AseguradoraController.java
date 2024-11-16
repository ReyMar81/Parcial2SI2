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
import GestionDocumental.DTO.AseguradoraDTO;
import GestionDocumental.entity.Aseguradora;
import GestionDocumental.mapper.AseguradoraMapper;
import GestionDocumental.service.AseguradoraService;

@RestController
@RequestMapping("/aseguradora")
public class AseguradoraController {
    private final AseguradoraService aseguradoraService;
    private final AseguradoraMapper aseguradoraMapper;

    public AseguradoraController(AseguradoraService aseguradoraService, AseguradoraMapper aseguradoraMapper) {
        this.aseguradoraService = aseguradoraService;
        this.aseguradoraMapper = aseguradoraMapper;
    }

    // Endpoint para crear una nueva aseguradora
    @PostMapping
    public ResponseEntity<AseguradoraDTO> crearAseguradora(@RequestBody AseguradoraDTO aseguradoraDTO) {
        Aseguradora aseguradora = aseguradoraMapper.toEntity(aseguradoraDTO);
        Aseguradora nuevaAseguradora = aseguradoraService.guardarAseguradora(aseguradora);
        AseguradoraDTO nuevaAseguradoraDTO = aseguradoraMapper.toDto(nuevaAseguradora);
        return new ResponseEntity<>(nuevaAseguradoraDTO, HttpStatus.CREATED);
    }

    // Endpoint para obtener todas las aseguradoras
    @GetMapping
    public ResponseEntity<List<AseguradoraDTO>> obtenerTodasLasAseguradoras() {
        List<AseguradoraDTO> aseguradoras = aseguradoraService.obtenerAseguradoras()
                .stream()
                .map(aseguradoraMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(aseguradoras, HttpStatus.OK);
    }

    // Endpoint para obtener una aseguradora por ID
    @GetMapping("/{id}")
    public ResponseEntity<AseguradoraDTO> obtenerAseguradoraPorId(@PathVariable int id) {
        return aseguradoraService.obtenerAseguradoraPorId(id)
                .map(aseguradora -> new ResponseEntity<>(aseguradoraMapper.toDto(aseguradora), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para actualizar una aseguradora
    @PutMapping("/{id}")
    public ResponseEntity<AseguradoraDTO> actualizarAseguradora(@PathVariable int id,
            @RequestBody AseguradoraDTO aseguradoraDTO) {
        Aseguradora aseguradoraActualizada = aseguradoraService.actualizarAseguradora(
                id, aseguradoraDTO.getNombre(), aseguradoraDTO.getDescripcion(), aseguradoraDTO.getContacto());
        AseguradoraDTO aseguradoraActualizadaDTO = aseguradoraMapper.toDto(aseguradoraActualizada);
        return new ResponseEntity<>(aseguradoraActualizadaDTO, HttpStatus.OK);
    }

    // Endpoint para eliminar una aseguradora
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAseguradora(@PathVariable int id) {
        aseguradoraService.eliminarAseguradora(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
