package GestionDocumental.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import GestionDocumental.DTO.ArchivoDTO;
import GestionDocumental.Exception.CitaNotFoundException;
import GestionDocumental.Exception.CustomAccessDeniedException;
import GestionDocumental.Exception.HistorialMedicoNotFoundException;
import GestionDocumental.entity.Archivos;
import GestionDocumental.entity.Cita;
import GestionDocumental.entity.HistorialMedico;
import GestionDocumental.mapper.ArchivoMapper;
import GestionDocumental.repository.ArchivoRepository;
import GestionDocumental.repository.CitaRepository;
import GestionDocumental.repository.HistorialMedicoRepository;
import jakarta.transaction.Transactional;

@Service
public class ArchivoService {

    private final ArchivoRepository archivosRepository;
    private final CitaRepository citaRepository;
    private final HistorialMedicoRepository historialMedicoRepository;
    private final ArchivoMapper archivoMapper;

    public ArchivoService(ArchivoRepository archivosRepository, CitaRepository citaRepository,
            HistorialMedicoRepository historialMedicoRepository, ArchivoMapper archivoMapper) {
        this.archivosRepository = archivosRepository;
        this.citaRepository = citaRepository;
        this.historialMedicoRepository = historialMedicoRepository;
        this.archivoMapper = archivoMapper;
    }

    // Guardar un archivo y actualizar la fecha de actualización en el historial

    @Transactional // Asegura que cualquier excepción revertirá la transacción
    public ArchivoDTO guardarArchivo(ArchivoDTO archivoDTO) {
        Archivos archivo = archivoMapper.toEntity(archivoDTO);

        // Verificar y establecer la cita si se proporciona un idCita
        if (archivoDTO.getIdCita() != 0) {
            Cita cita = citaRepository.findById(archivoDTO.getIdCita())
                    .orElseThrow(() -> new CitaNotFoundException(archivoDTO.getIdCita()));
            archivo.setArchivoscita(cita);
        }

        // Verificar y establecer el historial médico si se proporciona un
        // idHistorialMedico
        if (archivoDTO.getIdHistorialMedico() != 0) {
            HistorialMedico historial = historialMedicoRepository.findById(archivoDTO.getIdHistorialMedico())
                    .orElseThrow(() -> new HistorialMedicoNotFoundException(archivoDTO.getIdHistorialMedico()));
            archivo.setArchivosHistorialMedico(historial);
        }

        Archivos archivoGuardado = archivosRepository.save(archivo);
        return archivoMapper.toDto(archivoGuardado);
    }

    // Obtener todos los archivos
    public List<ArchivoDTO> obtenerTodosLosArchivos() {
        List<Archivos> archivos = archivosRepository.findAll();
        return archivos.stream().map(archivoMapper::toDto).collect(Collectors.toList());
    }

    // Obtener un archivo por ID
    public Optional<ArchivoDTO> obtenerArchivoPorId(int id) {
        return archivosRepository.findById(id).map(archivoMapper::toDto);
    }

    public List<ArchivoDTO> obtenerArchivosPorHistorial(int historialMedicoId, int medicoId) {
        // Verificar si existe una cita en proceso para este historial médico y médico
        boolean existeCita = citaRepository.existsByHistorialMedicoAndMedicoAndEstado(historialMedicoId, medicoId,
                "En proceso");

        if (!existeCita) {
            throw new CustomAccessDeniedException("El médico no tiene acceso a los archivos de este paciente.");
        }

        // Si la verificación pasa, obtener los archivos
        List<Archivos> archivos = archivosRepository.findByArchivosHistorialMedicoID(historialMedicoId);
        return archivos.stream().map(archivoMapper::toDto).collect(Collectors.toList());
    }

}
