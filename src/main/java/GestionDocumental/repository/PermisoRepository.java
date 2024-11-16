package GestionDocumental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Permiso;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

}
