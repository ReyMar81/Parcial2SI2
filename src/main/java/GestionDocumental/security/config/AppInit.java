package GestionDocumental.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import GestionDocumental.entity.Rol;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.RolRepository;
import GestionDocumental.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import java.util.Optional;

@Component
public class AppInit {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolesRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.password}")
    private String adminPassword; // La contraseña del usuario admin se define en application.properties

    @PostConstruct
    private void init() {
        System.out.println("Admin Password: " + adminPassword);
        createRolesAndAdminUser();
    }

    private void createRolesAndAdminUser() {
        // Crear los roles ADMINISTRADOR, MEDICO y PACIENTE
        createRoleIfNotExists("ADMINISTRADOR", "Rol de superusuario con todos los permisos.");
        createRoleIfNotExists("MEDICO", "Rol de médico, con acceso a gestionar citas y subir documentación.");
        createRoleIfNotExists("PACIENTE", "Rol de paciente, con acceso a su historial y reservar citas.");

        // Crear el usuario admin con el rol de ADMINISTRADOR
        createAdminUser();
    }

    // Método para crear un rol si no existe
    private void createRoleIfNotExists(String roleName, String description) {
        Optional<Rol> existingRole = rolesRepository.findByNombre(roleName);
        if (existingRole.isEmpty()) {
            Rol role = new Rol();
            role.setNombre(roleName);
            role.setDescripcion(description);
            rolesRepository.save(role);
            System.out.println("Rol " + roleName + " creado con éxito.");
        } else {
            System.out.println("El rol " + roleName + " ya existe.");
        }
    }

    // Método para crear el usuario administrador
    private void createAdminUser() {
        Optional<Rol> adminRoleOptional = rolesRepository.findByNombre("ADMINISTRADOR");
        if (adminRoleOptional.isEmpty()) {
            throw new IllegalStateException("Rol ADMINISTRADOR no encontrado. No se puede crear el usuario admin.");
        }

        Rol adminRole = adminRoleOptional.get();
        Optional<Usuario> existingUser = usuarioRepository.findByUserName("admin");

        if (existingUser.isEmpty()) {
            Usuario adminUser = new Usuario();
            adminUser.setUserName("admin"); // Nombre de usuario de la cuenta maestra
            adminUser.setPassword(passwordEncoder.encode(adminPassword)); // Contraseña cifrada
            adminUser.setUsuariorol(adminRole); // Asignar el rol de ADMINISTRADOR
            usuarioRepository.save(adminUser);
            System.out.println("Usuario admin creado con éxito.");
        } else {
            System.out.println("El usuario admin ya existe.");
        }
    }
}
