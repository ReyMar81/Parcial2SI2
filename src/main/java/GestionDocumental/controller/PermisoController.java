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
import GestionDocumental.DTO.PermisoDTO;
import GestionDocumental.entity.Permiso;
import GestionDocumental.mapper.PermisoMapper;
import GestionDocumental.service.PermisoService;

@RestController
@RequestMapping("/permisos")
public class PermisoController {
    private final PermisoService permisoService;
    private final PermisoMapper permisoMapper;

    public PermisoController(PermisoService permisoService, PermisoMapper permisoMapper) {
        this.permisoService = permisoService;
        this.permisoMapper = permisoMapper;
    }

    // Endpoint para crear un permiso
    @PostMapping
    public ResponseEntity<PermisoDTO> crearPermiso(@RequestBody PermisoDTO permisoDTO) {
        Permiso permiso = permisoMapper.toEntity(permisoDTO);
        Permiso nuevoPermiso = permisoService.guardarPermiso(permiso);
        PermisoDTO nuevoPermisoDTO = permisoMapper.toDto(nuevoPermiso);
        return new ResponseEntity<>(nuevoPermisoDTO, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los permisos
    @GetMapping
    public ResponseEntity<List<PermisoDTO>> obtenerTodosLosPermisos() {
        List<PermisoDTO> permisos = permisoService.obtenerPermisos()
                .stream()
                .map(permisoMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }

    // Endpoint para obtener un permiso por ID
    @GetMapping("/{id}")
    public ResponseEntity<PermisoDTO> obtenerPermisoPorId(@PathVariable int id) {
        return permisoService.obtenerPermisoPorId(id)
                .map(permiso -> new ResponseEntity<>(permisoMapper.toDto(permiso), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para actualizar un permiso
    @PutMapping("/{id}")
    public ResponseEntity<PermisoDTO> actualizarPermiso(@PathVariable int id, @RequestBody PermisoDTO permisoDTO) {
        Permiso permisoActualizado = permisoService.actualizarPermiso(id, permisoDTO.getNombre(),
                permisoDTO.getDescripcion());
        PermisoDTO permisoActualizadoDTO = permisoMapper.toDto(permisoActualizado);
        return new ResponseEntity<>(permisoActualizadoDTO, HttpStatus.OK);
    }

    // Endpoint para eliminar un permiso
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable int id) {
        permisoService.eliminarPermiso(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
