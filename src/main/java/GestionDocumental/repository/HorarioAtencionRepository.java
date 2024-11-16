package GestionDocumental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.HorarioAtencion;

@Repository
public interface HorarioAtencionRepository extends JpaRepository<HorarioAtencion, Integer> {

}
