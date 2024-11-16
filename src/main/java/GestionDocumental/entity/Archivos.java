package GestionDocumental.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Archivos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String RutaURL;
    private String TipoDocumento;
    private LocalDate fechaSubida;

    // Relacion con Citas
    @ManyToOne
    @JoinColumn(name = "IDCita")
    private Cita archivoscita;

    // Relacion con HistorialMedico
    @ManyToOne
    @JoinColumn(name = "IDHistorialMedico")
    private HistorialMedico archivosHistorialMedico;
}