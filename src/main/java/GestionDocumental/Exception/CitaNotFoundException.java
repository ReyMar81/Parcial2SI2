package GestionDocumental.Exception;

public class CitaNotFoundException extends RuntimeException {
    public CitaNotFoundException(int idCita) {
        super("Cita no encontrada con ID: " + idCita);
    }
}
