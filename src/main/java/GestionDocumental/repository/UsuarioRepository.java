package GestionDocumental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import GestionDocumental.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByUserName(String userName);

    List<Usuario> findByUsuariorol_ID(int rolId);

    boolean existsByUserName(String userName);
}
