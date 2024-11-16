package GestionDocumental.service;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import GestionDocumental.entity.AccesoDocumentacion;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.AccesoDocumentacionRepository;

@Service
public class AccesoDocumentacionService {

    @Autowired
    private AccesoDocumentacionRepository accesoDocumentacionRepository;

    public void registrarAcceso(String tipoDocumento, String accion, Usuario usuario) {
        AccesoDocumentacion acceso = new AccesoDocumentacion();
        acceso.setTipoDocumento(tipoDocumento);
        acceso.setAccion(accion); // Accion como "subida" o "consulta"
        acceso.setFechaAcceso(LocalDate.now());
        acceso.setHoraAcceso(LocalTime.now());
        acceso.setAccesodocumentacionusuario(usuario);

        accesoDocumentacionRepository.save(acceso);
    }

    public void registrarInicioSesion(Usuario usuario) {
        AccesoDocumentacion acceso = new AccesoDocumentacion();
        acceso.setTipoDocumento("Ninguno");
        acceso.setTipoEvento("Inicio de Sesión");
        acceso.setAccion("inicio");
        acceso.setFechaAcceso(LocalDate.now());
        acceso.setHoraAcceso(LocalTime.now());
        acceso.setAccesodocumentacionusuario(usuario);

        accesoDocumentacionRepository.save(acceso);
    }
}
