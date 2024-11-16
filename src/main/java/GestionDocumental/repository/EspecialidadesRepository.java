package GestionDocumental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Especialidades;

@Repository
public interface EspecialidadesRepository extends JpaRepository<Especialidades, Integer> {

}
