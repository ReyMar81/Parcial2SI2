package GestionDocumental.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import GestionDocumental.DTO.ArchivoDTO;
import GestionDocumental.entity.Usuario;
import GestionDocumental.service.AccesoDocumentacionService;
import GestionDocumental.service.ArchivoService;
import GestionDocumental.service.UsuarioService;

@RestController
@RequestMapping("/archivos")
public class ArchivosController {

    private final ArchivoService archivosService;
    private final UsuarioService usuarioService;
    private final AccesoDocumentacionService accesoDocumentacionService;

    public ArchivosController(ArchivoService archivosService, UsuarioService usuarioService,
            AccesoDocumentacionService accesoDocumentacionService) {
        this.archivosService = archivosService;
        this.usuarioService = usuarioService;
        this.accesoDocumentacionService = accesoDocumentacionService;
    }

    // Endpoint para subir un archivo nuevo
    @PostMapping("/subir")
    public ResponseEntity<ArchivoDTO> subirArchivo(@RequestBody ArchivoDTO archivoDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        ArchivoDTO archivoGuardadoDTO = archivosService.guardarArchivo(archivoDTO);

        // Obtener el usuario autenticado
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(userDetails.getUsername());

        // Registrar el acceso
        accesoDocumentacionService.registrarAcceso("Archivo", "subida", usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(archivoGuardadoDTO);
    }

    // Endpoint para obtener todos los archivos
    @GetMapping
    public List<ArchivoDTO> obtenerTodosLosArchivos() {
        return archivosService.obtenerTodosLosArchivos();
    }

    // Endpoint para obtener un archivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ArchivoDTO> obtenerArchivoPorId(@PathVariable int id) {
        return archivosService.obtenerArchivoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoint para obtener archivos por historial médico con verificación de cita
    // en proceso
    @GetMapping("/historial/{historialMedicoId}")
    public ResponseEntity<List<ArchivoDTO>> obtenerArchivosPorHistorial(
            @PathVariable int historialMedicoId, @AuthenticationPrincipal UserDetails userDetails) {

        int medicoId = Integer.parseInt(userDetails.getUsername());
        List<ArchivoDTO> archivos = archivosService.obtenerArchivosPorHistorial(historialMedicoId, medicoId);
        return ResponseEntity.ok(archivos);
    }

    @GetMapping("/historial/{historialMedicoId}/medico/{medicoId}")
    public ResponseEntity<List<ArchivoDTO>> obtenerArchivosPorHistorial(
            @PathVariable int historialMedicoId,
            @PathVariable int medicoId) {
        List<ArchivoDTO> archivos = archivosService.obtenerArchivosPorHistorial(historialMedicoId, medicoId);
        return ResponseEntity.ok(archivos);
    }

}
