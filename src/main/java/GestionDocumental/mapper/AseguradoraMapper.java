package GestionDocumental.mapper;

import org.springframework.stereotype.Component;

import GestionDocumental.DTO.AseguradoraDTO;
import GestionDocumental.entity.Aseguradora;

@Component
public class AseguradoraMapper {
    public Aseguradora toEntity(AseguradoraDTO dto) {
        Aseguradora aseguradora = new Aseguradora();
        aseguradora.setID(dto.getId());
        aseguradora.setNombre(dto.getNombre());
        aseguradora.setDescripcion(dto.getDescripcion());
        aseguradora.setContacto(dto.getContacto());
        return aseguradora;
    }

    public AseguradoraDTO toDto(Aseguradora aseguradora) {
        AseguradoraDTO dto = new AseguradoraDTO();
        dto.setId(aseguradora.getID());
        dto.setNombre(aseguradora.getNombre());
        dto.setDescripcion(aseguradora.getDescripcion());
        dto.setContacto(aseguradora.getContacto());
        return dto;
    }
}
