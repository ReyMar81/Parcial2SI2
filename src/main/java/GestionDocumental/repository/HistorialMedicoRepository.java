package GestionDocumental.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.HistorialMedico;
import GestionDocumental.entity.Pacientes;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    Optional<HistorialMedico> findByHistorialMedicoPaciente(Pacientes historialMedicoPaciente);

    @Query("SELECT u.id FROM HistorialMedico h " +
            "JOIN h.historialMedicoPaciente p " +
            "JOIN p.pacienteusuario u " +
            "WHERE h.id = :historialMedicoId")
    Optional<Integer> findUsuarioIdByHistorialMedicoId(@Param("historialMedicoId") int historialMedicoId);
}
