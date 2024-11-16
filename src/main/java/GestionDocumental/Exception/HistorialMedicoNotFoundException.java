package GestionDocumental.Exception;

public class HistorialMedicoNotFoundException extends RuntimeException {
    public HistorialMedicoNotFoundException(int idHistorialMedico) {
        super("Historial Médico no encontrado con ID: " + idHistorialMedico);
    }
}
