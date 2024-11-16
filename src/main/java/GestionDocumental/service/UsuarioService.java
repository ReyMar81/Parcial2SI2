package GestionDocumental.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import GestionDocumental.DTO.UsuarioAdminUpdateDTO;
import GestionDocumental.DTO.UsuarioMedicoUpdateDTO;
import GestionDocumental.DTO.UsuarioPacienteDTO;
import GestionDocumental.Exception.AseguradoraNotFoundException;
import GestionDocumental.Exception.EspecialidadNotFoundException;
import GestionDocumental.Exception.RolNotFoundException;
import GestionDocumental.Exception.UsernameAlreadyExistsException;
import GestionDocumental.Exception.UsuarioNotFoundException;
import GestionDocumental.entity.Aseguradora;
import GestionDocumental.entity.Especialidades;
import GestionDocumental.entity.Pacientes;
import GestionDocumental.entity.Rol;
import GestionDocumental.entity.Usuario;
import GestionDocumental.mapper.UsuarioMapper;
import GestionDocumental.repository.AseguradoraRepository;
import GestionDocumental.repository.PacientesRepository;
import GestionDocumental.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolService rolesService;
    private final AseguradoraRepository aseguradorasRepository;
    private final PacientesRepository pacientesRepository;
    private final HistorialMedicoService historialMedicoService;
    private final EspecialidadesService especialidadesService;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, RolService rolesService,
            AseguradoraRepository aseguradorasRepository,
            PacientesRepository pacientesRepository,
            HistorialMedicoService historialMedicoService,
            EspecialidadesService especialidadesService,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolesService = rolesService;
        this.aseguradorasRepository = aseguradorasRepository;
        this.pacientesRepository = pacientesRepository;
        this.historialMedicoService = historialMedicoService;
        this.especialidadesService = especialidadesService;
        this.passwordEncoder = passwordEncoder;
    }

    // Método para registrar un usuario con rol de paciente
    public Usuario registrarUsuarioPaciente(UsuarioPacienteDTO usuarioPacienteDTO) {
        // Verificar si el username ya existe
        if (usuarioRepository.existsByUserName(usuarioPacienteDTO.getUserName())) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        Usuario usuario = UsuarioMapper.toUsuarioFromPacienteDTO(usuarioPacienteDTO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Rol rol = rolesService.obtenerRolPorId(usuarioPacienteDTO.getRolId())
                .orElseThrow(
                        () -> new RolNotFoundException("Rol no encontrado con ID: " + usuarioPacienteDTO.getRolId()));
        usuario.setUsuariorol(rol);
        usuario = usuarioRepository.save(usuario);
        Aseguradora aseguradora = aseguradorasRepository.findById(usuarioPacienteDTO.getIdAseguradora())
                .orElseThrow(() -> new AseguradoraNotFoundException(
                        "Aseguradora no encontrada con ID: " + usuarioPacienteDTO.getIdAseguradora()));
        Pacientes paciente = UsuarioMapper.toPacienteFromUsuarioDTO(usuarioPacienteDTO, usuario, aseguradora);
        paciente = pacientesRepository.save(paciente);
        historialMedicoService.crearHistorialMedicoParaPaciente(usuario);
        return usuario;
    }

    // Método de actualización completo para el administrador
    public Usuario actualizarUsuarioComoAdmin(int id, UsuarioAdminUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si el username ya está en uso por otro usuario
        if (!usuario.getUserName().equals(dto.getUserName()) &&
                usuarioRepository.existsByUserName(dto.getUserName())) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        // Actualizar campos básicos del usuario
        usuario.setUserName(dto.getUserName());
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        // Obtener y asignar el rol deseado
        Rol nuevoRol = rolesService.obtenerRolPorId(dto.getRolId())
                .orElseThrow(() -> new RolNotFoundException("Rol no encontrado con ID: " + dto.getRolId()));
        usuario.setUsuariorol(nuevoRol);

        // Manejo del rol "medico" y asignación de especialidad
        if ("medico".equalsIgnoreCase(nuevoRol.getNombre())) {
            Especialidades especialidad = especialidadesService.obtenerEspecialidadPorId(dto.getEspecialidadId())
                    .orElseThrow(() -> new EspecialidadNotFoundException(
                            "Especialidad no encontrada con ID: " + dto.getEspecialidadId()));
            usuario.setUsuarioespecialidades(especialidad);
        } else {
            usuario.setUsuarioespecialidades(null);
        }

        // Manejo específico para el rol "paciente"
        if ("paciente".equalsIgnoreCase(nuevoRol.getNombre())) {
            Pacientes paciente = pacientesRepository.findByPacienteusuario_ID(usuario.getID()).orElse(null);

            if (paciente == null) {
                paciente = new Pacientes();
                paciente.setPacienteusuario(usuario);
            }

            paciente.setFechaNacimiento(dto.getFechaNacimiento());
            paciente.setPacienteaseguradora(
                    aseguradorasRepository.findById(dto.getIdAseguradora())
                            .orElseThrow(() -> new AseguradoraNotFoundException(
                                    "Aseguradora no encontrada con ID: " + dto.getIdAseguradora())));

            pacientesRepository.save(paciente);
        }

        return usuarioRepository.save(usuario);
    }

    // Método de actualización para el médico
    public Usuario actualizarUsuarioComoMedico(int id, UsuarioMedicoUpdateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuario no encontrado con ID: " + id));

        // Verificar si el username ya está en uso por otro usuario
        if (!usuario.getUserName().equals(dto.getUserName()) &&
                usuarioRepository.existsByUserName(dto.getUserName())) {
            throw new UsernameAlreadyExistsException("El nombre de usuario ya está en uso");
        }

        // Actualiza solo los campos permitidos para el médico
        usuario.setUserName(dto.getUserName());
        usuario.setEmail(dto.getEmail());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario obtenerUsuarioPorNombre(String username) {
        return usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }

    public List<Usuario> obtenerUsuariosPorRol(int rolId) {
        return usuarioRepository.findByUsuariorol_ID(rolId);
    }

    // Obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }
}
