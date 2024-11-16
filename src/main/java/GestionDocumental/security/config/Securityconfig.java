package GestionDocumental.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import GestionDocumental.security.service.UsuarioAuthService;

@Configuration
@EnableWebSecurity
public class Securityconfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter; // Inyecta el filtro JWT

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**").permitAll()
                        .requestMatchers("/auth/**").permitAll() // Permitir acceso sin autenticación para rutas de
                                                                 // autenticación
                        .requestMatchers("/citas/**").hasAnyAuthority("reservar_cita", "administrar_sistema")
                        .requestMatchers("/citaHora/**").hasAnyAuthority("reservar_cita", "administrar_sistema")
                        .anyRequest().permitAll() // Las demás rutas permiten acceso sin autentificación
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Configuración
                                                                                                               // sin
                                                                                                               // estado,
                                                                                                               // sin
                                                                                                               // sesión

        // Añadir el filtro JWT antes de que la autenticación estándar de Spring ocurra
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder,
            UsuarioAuthService userService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http
                .getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
