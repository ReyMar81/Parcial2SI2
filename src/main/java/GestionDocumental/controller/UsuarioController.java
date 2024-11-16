package GestionDocumental.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import GestionDocumental.DTO.UsuarioAdminUpdateDTO;
import GestionDocumental.DTO.UsuarioMedicoUpdateDTO;
import GestionDocumental.DTO.UsuarioPacienteDTO;
import GestionDocumental.entity.Usuario;
import GestionDocumental.mapper.UsuarioMapper;
import GestionDocumental.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint para registrar un usuario con rol de paciente
    @PostMapping("/registro-paciente")
    public ResponseEntity<UsuarioPacienteDTO> registrarUsuarioPaciente(
            @RequestBody UsuarioPacienteDTO usuarioPacienteDTO) {
        // Llama al servicio para registrar el usuario con rol de paciente
        Usuario usuario = usuarioService.registrarUsuarioPaciente(usuarioPacienteDTO);

        // Convertir el usuario creado a DTO para la respuesta
        UsuarioPacienteDTO usuarioRespuesta = UsuarioMapper.toUsuarioPacienteDTO(usuario);

        return ResponseEntity.ok(usuarioRespuesta);
    }

    // Endpoint para actualizar un usuario como administrador
    @PutMapping("/admin/{id}")
    public ResponseEntity<UsuarioAdminUpdateDTO> actualizarUsuarioComoAdmin(
            @PathVariable int id, @RequestBody UsuarioAdminUpdateDTO usuarioAdminDTO) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuarioComoAdmin(id, usuarioAdminDTO);

        // Convertir el usuario actualizado a UsuarioAdminUpdateDTO para la respuesta
        UsuarioAdminUpdateDTO usuarioRespuesta = UsuarioMapper.toUsuarioAdminUpdateDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioRespuesta);
    }

    // Endpoint para actualizar un usuario como médico
    @PutMapping("/medico/{id}")
    public ResponseEntity<UsuarioMedicoUpdateDTO> actualizarUsuarioComoMedico(
            @PathVariable int id, @RequestBody UsuarioMedicoUpdateDTO usuarioMedicoDTO) {
        Usuario usuarioActualizado = usuarioService.actualizarUsuarioComoMedico(id, usuarioMedicoDTO);

        // Convertir el usuario actualizado a UsuarioMedicoUpdateDTO para la respuesta
        UsuarioMedicoUpdateDTO usuarioRespuesta = UsuarioMapper.toUsuarioMedicoUpdateDTO(usuarioActualizado);

        return ResponseEntity.ok(usuarioRespuesta);
    }

    // Endpoint para obtener usuarios por ID de rol
    @GetMapping("/rol/{rolId}")
    public ResponseEntity<List<Usuario>> obtenerUsuariosPorRol(@PathVariable int rolId) {
        try {
            List<Usuario> usuarios = usuarioService.obtenerUsuariosPorRol(rolId);
            if (usuarios.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
