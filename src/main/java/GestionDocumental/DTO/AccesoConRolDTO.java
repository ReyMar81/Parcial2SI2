package GestionDocumental.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import GestionDocumental.entity.AccesoDocumentacion;
import lombok.Data;

@Data
public class AccesoConRolDTO {
    private int id;
    private String tipoDocumento;
    private String accion;
    private LocalDate fechaAcceso;
    private LocalTime horaAcceso;
    private int usuarioId;
    private String rol;

    public AccesoConRolDTO(AccesoDocumentacion acceso, String rol) {
        this.id = acceso.getID();
        this.tipoDocumento = acceso.getTipoDocumento();
        this.accion = acceso.getAccion();
        this.fechaAcceso = acceso.getFechaAcceso();
        this.horaAcceso = acceso.getHoraAcceso();
        this.usuarioId = acceso.getAccesodocumentacionusuario().getID();
        this.rol = rol;
    }
}
