package GestionDocumental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class RolPermisos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    // Relacion con Roles
    @ManyToOne
    @JoinColumn(name = "IDRol")
    private Rol rol;

    // Relacion con Permisos
    @ManyToOne
    @JoinColumn(name = "IDPermisos")
    private Permiso permisos;
}
