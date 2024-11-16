package GestionDocumental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.CitaHora;
import jakarta.transaction.Transactional;

@Repository
public interface CitaHoraRepository extends JpaRepository<CitaHora, Integer> {

        // Consulta para encontrar citas por especialidad y usuario
        @Query("SELECT c FROM CitaHora c " +
                        "JOIN c.horarioAtencion h " +
                        "JOIN h.horarioatencionusuario u " +
                        "JOIN u.usuarioespecialidades e " +
                        "WHERE e.ID = :idEspecialidad AND u.ID = :idUsuario")
        List<CitaHora> findCitasByEspecialidadAndUsuario(@Param("idEspecialidad") int idEspecialidad,
                        @Param("idUsuario") int idUsuario);

        // Método para eliminar todas las citas por ID de HorarioAtencion
        @Modifying
        @Transactional
        @Query("DELETE FROM CitaHora c WHERE c.horarioAtencion.ID = :horarioAtencionId")
        void deleteByHorarioAtencionId(@Param("horarioAtencionId") int horarioAtencionId);

        // Consulta para encontrar citas disponibles (libres)
        @Query("SELECT c FROM CitaHora c WHERE c.disponibilidad = 'Libre'")
        List<CitaHora> findCitasLibres();
}
