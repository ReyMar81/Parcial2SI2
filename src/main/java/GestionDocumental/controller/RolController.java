package GestionDocumental.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import GestionDocumental.DTO.RolDTO;
import GestionDocumental.entity.Rol;
import GestionDocumental.mapper.RolMapper;
import GestionDocumental.service.RolService;

@RestController
@RequestMapping("/roles")
public class RolController {

    private final RolService rolService;
    private final RolMapper rolMapper;

    public RolController(RolService rolService, RolMapper rolMapper) {
        this.rolService = rolService;
        this.rolMapper = rolMapper;
    }

    // Endpoint para crear un nuevo rol
    @PostMapping
    public ResponseEntity<RolDTO> crearRol(@RequestBody RolDTO rolDTO) {
        Rol rol = rolMapper.toEntity(rolDTO);
        Rol nuevoRol = rolService.guardarRol(rol);
        RolDTO nuevoRolDTO = rolMapper.toDto(nuevoRol);
        return new ResponseEntity<>(nuevoRolDTO, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los roles
    @GetMapping
    public ResponseEntity<List<RolDTO>> obtenerTodosLosRoles() {
        List<RolDTO> roles = rolService.obtenerRoles()
                .stream()
                .map(rolMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    // Endpoint para obtener un rol por ID
    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> obtenerRolPorId(@PathVariable int id) {
        return rolService.obtenerRolPorId(id)
                .map(rol -> new ResponseEntity<>(rolMapper.toDto(rol), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Endpoint para actualizar un rol
    @PutMapping("/{id}")
    public ResponseEntity<RolDTO> actualizarRol(@PathVariable int id, @RequestBody RolDTO rolDTO) {
        Rol rolActualizado = rolService.actualizarRol(id, rolDTO.getNombre(), rolDTO.getDescripcion());
        RolDTO rolActualizadoDTO = rolMapper.toDto(rolActualizado);
        return new ResponseEntity<>(rolActualizadoDTO, HttpStatus.OK);
    }

    // Endpoint para eliminar un rol
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarRol(@PathVariable int id) {
        rolService.eliminarRol(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
