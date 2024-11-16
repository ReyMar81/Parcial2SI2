package GestionDocumental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import GestionDocumental.entity.RolPermisos;

@Repository
public interface RolPermisosRepository extends JpaRepository<RolPermisos, Integer> {
    List<RolPermisos> findByRol_ID(int rolID);

    Optional<RolPermisos> findByRol_IDAndPermisos_ID(int rolID, int permisoID);
}
