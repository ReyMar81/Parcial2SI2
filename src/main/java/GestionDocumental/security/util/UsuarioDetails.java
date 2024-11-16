package GestionDocumental.security.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import GestionDocumental.entity.Usuario;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
public class UsuarioDetails implements UserDetails {

    private final Usuario usuario;
    private final List<String> permisos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Añadir el rol como autoridad
        if (usuario.getUsuariorol() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getUsuariorol().getNombre()));
        }

        // Añadir cada permiso como una autoridad
        permisos.forEach(permission -> authorities.add(new SimpleGrantedAuthority(permission)));

        return authorities;
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
