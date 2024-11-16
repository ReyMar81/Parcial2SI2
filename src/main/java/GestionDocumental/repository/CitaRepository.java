package GestionDocumental.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Cita;
import GestionDocumental.entity.Usuario;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

        // Consulta para obtener citas por paciente y fecha de creación
        boolean existsByCitausuario_IDAndFechaCreacion(int usuarioID, LocalDate fechaCreacion);

        // Consulta para obtener citas por paciente y fecha de cita
        boolean existsByCitausuario_IDAndFechaCita(int usuarioID, LocalDate fechaCita);

        // Buscar citas que deben pasar a "En Proceso"
        List<Cita> findByFechaCitaAndHoraCitaAndEstado(LocalDate fecha, LocalTime hora, String estado);

        // Verificar si hay una cita en proceso para el médico y el usuario de la cita
        // (paciente)
        @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
                        "FROM cita c " +
                        "JOIN usuario u ON c.idusuario = u.id " +
                        "JOIN pacientes p ON u.id = p.idusuario " +
                        "JOIN historial_medico h ON p.id = h.idpaciente " +
                        "WHERE h.id = :historialMedicoId " +
                        "AND u.id = :medicoId " +
                        "AND c.estado = :estado", nativeQuery = true)
        boolean existsByHistorialMedicoAndMedicoAndEstado(@Param("historialMedicoId") int historialMedicoId,
                        @Param("medicoId") int medicoId,
                        @Param("estado") String estado);

        // Buscar citas que deben pasar a "Terminado"
        List<Cita> findByFechaCitaAndEstado(LocalDate fechaCita, String Estado);

        Optional<Cita> findTopByCitausuarioAndEstadoOrderByFechaCitaDesc(Usuario citausuario, String estado);

        // Consulta para obtener citas en un rango de fechas y horas
        @Query("SELECT c FROM Cita c WHERE c.fechaCita BETWEEN :startDate AND :endDate " +
                        "AND c.horaCita BETWEEN :startTime AND :endTime")
        List<Cita> findByFechaCitaBetweenAndHoraCitaBetween(
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime);
}
