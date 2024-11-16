package GestionDocumental.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.AccesoDocumentacion;

@Repository
public interface AccesoDocumentacionRepository extends JpaRepository<AccesoDocumentacion, Integer> {

    List<AccesoDocumentacion> findByAccesodocumentacionusuario_ID(int usuarioID);

    List<AccesoDocumentacion> findByFechaAcceso(LocalDate fechaAcceso);
}
