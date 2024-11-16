package GestionDocumental.mapper;

import GestionDocumental.DTO.UsuarioAdminUpdateDTO;
import GestionDocumental.DTO.UsuarioMedicoUpdateDTO;
import GestionDocumental.DTO.UsuarioPacienteDTO;
import GestionDocumental.entity.Aseguradora;
import GestionDocumental.entity.Pacientes;
import GestionDocumental.entity.Usuario;

public class UsuarioMapper {

    // Convierte UsuarioPacienteDTO a Usuario
    public static Usuario toUsuarioFromPacienteDTO(UsuarioPacienteDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setUserName(dto.getUserName());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());
        usuario.setNombre(dto.getNombre());
        usuario.setApellidoPaterno(dto.getApellidoPaterno());
        usuario.setApellidoMaterno(dto.getApellidoMaterno());
        return usuario;
    }

    // Convierte UsuarioPacienteDTO a Pacientes
    public static Pacientes toPacienteFromUsuarioDTO(UsuarioPacienteDTO dto, Usuario usuario,
            Aseguradora aseguradora) {
        Pacientes paciente = new Pacientes();
        paciente.setFechaNacimiento(dto.getFechaNacimiento());
        paciente.setPacienteusuario(usuario); // Relaciona el paciente con el usuario
        paciente.setPacienteaseguradora(aseguradora); // Asigna la aseguradora
        return paciente;
    }

    public static UsuarioPacienteDTO toUsuarioPacienteDTO(Usuario usuario) {
        UsuarioPacienteDTO dto = new UsuarioPacienteDTO();
        dto.setUserName(usuario.getUserName());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setRolId(usuario.getUsuariorol().getID());
        // Asigna otros campos si es necesario, como aseguradora o fechaNacimiento si
        // están disponibles
        return dto;
    }

    public static UsuarioAdminUpdateDTO toUsuarioAdminUpdateDTO(Usuario usuario) {
        UsuarioAdminUpdateDTO dto = new UsuarioAdminUpdateDTO();
        dto.setUserName(usuario.getUserName());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        dto.setRolId(usuario.getUsuariorol().getID());
        // Asigna otros campos si es necesario, como especialidad, fechaNacimiento, y
        // aseguradora
        return dto;
    }

    public static UsuarioMedicoUpdateDTO toUsuarioMedicoUpdateDTO(Usuario usuario) {
        UsuarioMedicoUpdateDTO dto = new UsuarioMedicoUpdateDTO();
        dto.setUserName(usuario.getUserName());
        dto.setEmail(usuario.getEmail());
        dto.setNombre(usuario.getNombre());
        dto.setApellidoPaterno(usuario.getApellidoPaterno());
        dto.setApellidoMaterno(usuario.getApellidoMaterno());
        return dto;
    }

}
