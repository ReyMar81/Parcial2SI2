package GestionDocumental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Aseguradora;

@Repository
public interface AseguradoraRepository extends JpaRepository<Aseguradora, Integer> {

}
