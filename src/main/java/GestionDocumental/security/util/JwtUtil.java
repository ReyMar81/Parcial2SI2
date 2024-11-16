package GestionDocumental.security.util;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;

@Component
public class JwtUtil {

    private final Key key;

    @Value("${jwt.expiration}")
    private int expiration; // Duración del token en milisegundos

    public JwtUtil() {
        this.key = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Genera una clave segura para HS512
    }

    // Método para generar el token JWT
    public String generateToken(String username, String role, int userId, List<String> permissions) {
        long now = System.currentTimeMillis();
        long expiryDate = now + expiration;

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("userId", userId)
                .claim("permissions", permissions)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expiryDate))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    // Método para extraer el nombre de usuario del token JWT
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> getPermissionsFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Obtener la lista de permisos del token
        Object permissionsObj = claims.get("permissions");
        List<String> permissions = new ArrayList<>();

        // Asegurarse de que 'permissionsObj' es una instancia de List y realizar el
        // casting correctamente
        if (permissionsObj instanceof List<?>) {
            permissions = (List<String>) permissionsObj; // Realizamos el cast correctamente
        } else {
            System.out.println("El token no contiene una lista de permisos válida.");
        }

        return permissions;
    }

    // Método para extraer el userId del token JWT
    public int getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Integer.class);
    }

    // Método para validar el token JWT
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            System.out.println("Token válido");
            return true;
        } catch (Exception e) {
            System.out.println("Token no válido: " + e.getMessage());
        }
        return false;
    }

}