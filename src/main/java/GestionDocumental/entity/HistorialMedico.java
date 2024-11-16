package GestionDocumental.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private LocalDate FechaCreacion;
    private LocalDate FechaActualizacion;

    // Relacion con Pacientes
    @OneToOne
    @JoinColumn(name = "IDPaciente")
    private Pacientes historialMedicoPaciente;
}
