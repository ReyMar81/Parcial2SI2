package GestionDocumental.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    @Column(name = "UserName")
    private String userName;

    private String Email;
    @JsonIgnore
    private String Password;
    private String Nombre;
    private String ApellidoPaterno;
    private String ApellidoMaterno;

    // Relacion con roles
    @ManyToOne
    @JoinColumn(name = "IDRol")
    private Rol usuariorol;

    // Relacion con Especialidades
    @ManyToOne
    @JoinColumn(name = "IDEspecialidad")
    private Especialidades usuarioespecialidades;
}
