package GestionDocumental.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import GestionDocumental.DTO.AccesoConRolDTO;
import GestionDocumental.entity.AccesoDocumentacion;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.AccesoDocumentacionRepository;

@RestController
@RequestMapping("/accesos")
public class AccesoDocumentacionController {
    private final AccesoDocumentacionRepository accesoDocumentacionRepository;

    public AccesoDocumentacionController(AccesoDocumentacionRepository accesoDocumentacionRepository) {
        this.accesoDocumentacionRepository = accesoDocumentacionRepository;
    }

    // 1. Endpoint para obtener todos los accesos (bitácora completa)
    @GetMapping
    public List<AccesoConRolDTO> obtenerTodosLosAccesos() {
        List<AccesoDocumentacion> accesos = accesoDocumentacionRepository.findAll();

        // Convertir a DTO para incluir el rol del usuario
        return accesos.stream()
                .map(acceso -> {
                    Usuario usuario = acceso.getAccesodocumentacionusuario();
                    String rol = usuario.getUsuariorol().getNombre();
                    return new AccesoConRolDTO(acceso, rol);
                })
                .collect(Collectors.toList());
    }

    // 2. Endpoint para obtener accesos por fecha específica
    @GetMapping("/porFecha")
    public List<AccesoConRolDTO> obtenerAccesosPorFecha(@RequestParam LocalDate fecha) {
        List<AccesoDocumentacion> accesos = accesoDocumentacionRepository.findByFechaAcceso(fecha);

        return accesos.stream()
                .map(acceso -> {
                    Usuario usuario = acceso.getAccesodocumentacionusuario();
                    String rol = usuario.getUsuariorol().getNombre();
                    return new AccesoConRolDTO(acceso, rol);
                })
                .collect(Collectors.toList());
    }

    // 3. Endpoint para obtener accesos por usuario
    @GetMapping("/porUsuario/{usuarioId}")
    public List<AccesoConRolDTO> obtenerAccesosPorUsuario(@PathVariable int usuarioId) {
        List<AccesoDocumentacion> accesos = accesoDocumentacionRepository
                .findByAccesodocumentacionusuario_ID(usuarioId);

        return accesos.stream()
                .map(acceso -> {
                    Usuario usuario = acceso.getAccesodocumentacionusuario();
                    String rol = usuario.getUsuariorol().getNombre();
                    return new AccesoConRolDTO(acceso, rol);
                })
                .collect(Collectors.toList());
    }
}
