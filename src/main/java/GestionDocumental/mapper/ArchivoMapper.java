package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.ArchivoDTO;
import GestionDocumental.entity.Archivos;

@Component
public class ArchivoMapper {

    public Archivos toEntity(ArchivoDTO dto) {
        Archivos archivo = new Archivos();
        archivo.setRutaURL(dto.getRuta());
        archivo.setTipoDocumento(dto.getTipoDocumento());
        archivo.setFechaSubida(dto.getFechaSubida());
        // Configura las relaciones con `Cita` e `HistorialMedico` si es necesario
        return archivo;
    }

    public ArchivoDTO toDto(Archivos archivo) {
        ArchivoDTO dto = new ArchivoDTO();
        dto.setId(archivo.getID());
        dto.setRuta(archivo.getRutaURL());
        dto.setTipoDocumento(archivo.getTipoDocumento());
        dto.setFechaSubida(archivo.getFechaSubida());

        // Verificar si archivoscita no es null antes de acceder al ID
        if (archivo.getArchivoscita() != null) {
            dto.setIdCita(archivo.getArchivoscita().getID());
        } else {
            dto.setIdCita(0); // o cualquier valor que indique ausencia
        }

        // Verificar si archivosHistorialMedico no es null antes de acceder al ID
        if (archivo.getArchivosHistorialMedico() != null) {
            dto.setIdHistorialMedico(archivo.getArchivosHistorialMedico().getID());
        } else {
            dto.setIdHistorialMedico(0); // o cualquier valor que indique ausencia
        }

        return dto;
    }

}
