package GestionDocumental.controller;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import GestionDocumental.DTO.RolPermisoDTO;
import GestionDocumental.entity.RolPermisos;
import GestionDocumental.mapper.RolPermisosMapper;
import GestionDocumental.service.RolPermisosService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rolpermisos")
public class RolPermisosController {
    private final RolPermisosService rolPermisosService;
    private final RolPermisosMapper rolPermisosMapper;

    public RolPermisosController(RolPermisosService rolPermisosService, RolPermisosMapper rolPermisosMapper) {
        this.rolPermisosService = rolPermisosService;
        this.rolPermisosMapper = rolPermisosMapper;
    }

    // Endpoint para asignar un permiso a un rol
    @PostMapping
    public ResponseEntity<RolPermisoDTO> asignarPermisoARol(@RequestBody RolPermisoDTO rolPermisoDTO) {
        RolPermisos rolPermisos = rolPermisosService.asignarPermisoARol(rolPermisoDTO.getRolId(),
                rolPermisoDTO.getPermisoId());
        RolPermisoDTO nuevoRolPermisoDTO = rolPermisosMapper.toDto(rolPermisos);
        return new ResponseEntity<>(nuevoRolPermisoDTO, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los permisos de un rol específico
    @GetMapping("/{rolId}")
    public ResponseEntity<List<RolPermisoDTO>> obtenerPermisosPorRol(@PathVariable int rolId) {
        List<RolPermisoDTO> permisos = rolPermisosService.obtenerPermisosPorRol(rolId)
                .stream()
                .map(rolPermisosMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(permisos, HttpStatus.OK);
    }

    // Endpoint para eliminar un permiso de un rol
    @DeleteMapping
    public ResponseEntity<Void> eliminarPermisoDeRol(@RequestParam int rolId, @RequestParam int permisoId) {
        rolPermisosService.eliminarPermisoDeRol(rolId, permisoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
