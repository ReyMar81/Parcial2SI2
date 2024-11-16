package GestionDocumental.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;
    private LocalDate FechaNacimiento;

    // Relacion con Aseguradoras
    @ManyToOne
    @JoinColumn(name = "IDAseguradora")
    private Aseguradora pacienteaseguradora;

    // Relacion con Usuario
    @OneToOne
    @JoinColumn(name = "IDUsuario")
    private Usuario pacienteusuario;
}
