package GestionDocumental.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.RolPermisosRepository;
import GestionDocumental.repository.UsuarioRepository;
import GestionDocumental.security.util.UsuarioDetails;

@Service
public class UsuarioAuthService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolPermisosRepository rolPermisosRepository;

    // Método para obtener los permisos de un rol específico
    public List<String> getPermissionsByRoleId(int roleId) {
        return rolPermisosRepository.findByRol_ID(roleId).stream()
                .map(rolPermiso -> rolPermiso.getPermisos().getNombre()) // Obtener solo el nombre del permiso
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Obtener permisos del rol del usuario
        List<String> permisos = getPermissionsByRoleId(usuario.getUsuariorol().getID());

        // Crear `UsuarioDetails` con el usuario y la lista de permisos
        return new UsuarioDetails(usuario, permisos);
    }
}
