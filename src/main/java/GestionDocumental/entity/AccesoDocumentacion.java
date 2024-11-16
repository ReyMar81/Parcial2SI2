package GestionDocumental.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class AccesoDocumentacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String tipoEvento;
    private String tipoDocumento;
    private String accion;
    private LocalDate fechaAcceso;
    private LocalTime horaAcceso;

    // Relacion con Usuario
    @ManyToOne
    @JoinColumn(name = "IDUsuario")
    private Usuario accesodocumentacionusuario;
}
