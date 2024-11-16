package GestionDocumental.entity;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private LocalDate fechaCita;
    private LocalTime horaCita;
    private LocalDate fechaCreacion;
    private String estado;

    // Relacion con Usuario
    @ManyToOne
    @JoinColumn(name = "IDUsuario")
    private Usuario citausuario;

    // Relación uno a uno con CitaHora
    @OneToOne
    @JoinColumn(name = "IDCitaHora")
    private CitaHora citaHora;
}
