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
public class CitaHora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String disponibilidad;

    // Relacion con HorarioAtencion
    @ManyToOne
    @JoinColumn(name = "IDHorarioAtencion")
    private HorarioAtencion horarioAtencion;
}
