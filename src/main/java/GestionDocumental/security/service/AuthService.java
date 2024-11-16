package GestionDocumental.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import GestionDocumental.Exception.EspecialidadNotFoundException;
import GestionDocumental.Exception.RolNotFoundException;
import GestionDocumental.Exception.UsernameAlreadyExistsException;
import GestionDocumental.entity.Especialidades;
import GestionDocumental.entity.Rol;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.EspecialidadesRepository;
import GestionDocumental.repository.RolRepository;
import GestionDocumental.repository.UsuarioRepository;
import GestionDocumental.security.Auth.LoginRequest;
import GestionDocumental.security.Auth.RegisterRequest;
import GestionDocumental.security.util.JwtUtil;
import GestionDocumental.service.AccesoDocumentacionService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class AuthService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolRepository rolesRepository;

    @Autowired
    private EspecialidadesRepository especialidadesRepository;

    @Autowired
    private AccesoDocumentacionService accesoDocumentacionService;

    public String login(LoginRequest loginRequest) {
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Obtén el usuario desde la base de datos para acceder a su rol
            Usuario usuario = usuarioRepository.findByUserName(loginRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            // registro de inicio de sesion
            accesoDocumentacionService.registrarInicioSesion(usuario);
            // Obtén el rol del usuario
            String role = usuario.getUsuariorol().getNombre();
            int userId = usuario.getID();

            // Asignar permisos según el rol
            List<String> permissions = new ArrayList<>();
            if ("PACIENTE".equals(role)) {
                permissions.add("reservar_cita");
            } else if ("MEDICO".equals(role)) {
                permissions.add("ver_historial");
                permissions.add("editar_cita");
            } else if ("ADMINISTRADOR".equals(role)) {
                permissions.add("administrar_sistema");
                permissions.add("ver_reportes");
            }

            // Genera el token JWT incluyendo el rol, permisos
            return jwtUtil.generateToken(loginRequest.getUsername(), role, userId, permissions);
        } catch (Exception e) {
            throw e;
        }
    }

    public void register(RegisterRequest registerRequest) {
        // Verificar si el username ya existe
        if (usuarioRepository.findByUserName(registerRequest.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso.");
        }

        Usuario usuario = new Usuario();
        usuario.setUserName(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usuario.setEmail(registerRequest.getEmail());
        usuario.setNombre(registerRequest.getNombre());
        usuario.setApellidoPaterno(registerRequest.getApellidoPaterno());
        usuario.setApellidoMaterno(registerRequest.getApellidoMaterno());

        // Asigna el rol al usuario y lanza RolNotFoundException si no se encuentra
        if (registerRequest.getRolesId() != null) {
            Rol rol = rolesRepository.findById(registerRequest.getRolesId())
                    .orElseThrow(() -> new RolNotFoundException(
                            "Rol no encontrado con ID: " + registerRequest.getRolesId()));
            usuario.setUsuariorol(rol);
        }

        if (registerRequest.getRolesId() == 2) { // ID de Médico
            if (registerRequest.getEspecialidadId() == null) {
                throw new IllegalArgumentException("La especialidad es requerida para el rol de Médico.");
            }
            Especialidades especialidad = especialidadesRepository.findById(registerRequest.getEspecialidadId())
                    .orElseThrow(() -> new EspecialidadNotFoundException(
                            "Especialidad no encontrada con ID: " + registerRequest.getEspecialidadId()));
            usuario.setUsuarioespecialidades(especialidad);
        }

        usuarioRepository.save(usuario);
    }

}
