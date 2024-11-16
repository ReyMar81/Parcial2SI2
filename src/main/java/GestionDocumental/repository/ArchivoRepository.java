package GestionDocumental.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.Archivos;

@Repository
public interface ArchivoRepository extends JpaRepository<Archivos, Integer> {
    List<Archivos> findByArchivosHistorialMedicoID(int historialMedicoID);
}
