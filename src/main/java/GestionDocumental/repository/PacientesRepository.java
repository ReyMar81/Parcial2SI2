package GestionDocumental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Pacientes;

@Repository
public interface PacientesRepository extends JpaRepository<Pacientes, Integer> {
    Optional<Pacientes> findByPacienteusuario_ID(int idUsuario);
}
