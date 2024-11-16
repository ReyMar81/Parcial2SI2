package GestionDocumental.entity;

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
public class HorarioAtencion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String DiaSemana;
    private LocalTime HoraInicio;
    private LocalTime HoraFin;
    private int CantidadFichas;

    // Relacion con Usuario
    @ManyToOne
    @JoinColumn(name = "IDUsuario")
    private Usuario horarioatencionusuario;

    public enum DiaSemana {
        LUNES, MARTES, MIERCOLES, JUEVES, VIERNES
    }
}
