package GestionDocumental.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

import GestionDocumental.DTO.CitaHoraDTO;
import GestionDocumental.entity.CitaHora;
import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.entity.Usuario;
import GestionDocumental.mapper.CitaHoraMapper;
import GestionDocumental.repository.CitaHoraRepository;
import GestionDocumental.repository.HorarioAtencionRepository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CitaHoraService {

    private final CitaHoraRepository citaHoraRepository;
    private final HorarioAtencionRepository horarioAtencionRepository;
    private final CitaHoraMapper citaHoraMapper;

    public CitaHoraService(CitaHoraRepository citaHoraRepository,
            HorarioAtencionRepository horarioAtencionRepository,
            CitaHoraMapper citaHoraMapper) {
        this.citaHoraRepository = citaHoraRepository;
        this.horarioAtencionRepository = horarioAtencionRepository;
        this.citaHoraMapper = citaHoraMapper;
    }

    // Nuevo método para obtener todas las citas (libres y reservadas) por semana
    public Map<String, Object> obtenerTodasLasCitasPorSemana() {
        // Recupera todas las citas, sin filtrar
        List<CitaHora> todasLasCitas = citaHoraRepository.findAll();

        // Organizar las citas por día y especialidad
        Map<String, Map<String, List<CitaHoraDTO>>> citasPorSemana = new LinkedHashMap<>();

        for (CitaHora cita : todasLasCitas) {
            String diaSemana = cita.getHorarioAtencion().getDiaSemana();
            Usuario medico = cita.getHorarioAtencion().getHorarioatencionusuario();
            String nombreDoctor = medico.getNombre() + " " + medico.getApellidoPaterno();
            String nombreEspecialidad = medico.getUsuarioespecialidades().getNombre();

            // Convertir la cita en un DTO
            CitaHoraDTO citaDTO = citaHoraMapper.toDto(cita);
            citaDTO.setNombreDoctor(nombreDoctor);

            // Organizar la respuesta por día y especialidad
            citasPorSemana
                    .computeIfAbsent(diaSemana, k -> new LinkedHashMap<>())
                    .computeIfAbsent(nombreEspecialidad, k -> new ArrayList<>())
                    .add(citaDTO);
        }

        // Crear la estructura final de respuesta
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("citasPorDia", citasPorSemana);

        return respuesta;
    }

    // Obtener todas las citas de lunes a viernes, agrupadas por día, especialidad y
    // médico
    public Map<String, Object> obtenerCitasPorSemana() {
        // Obtener todas las citas de la base de datos
        List<CitaHora> citasLibres = citaHoraRepository.findCitasLibres(); // Recupera todas las citas disponibles

        // Estructura de respuesta para el frontend
        Map<String, Map<String, List<CitaHoraDTO>>> citasPorDia = new LinkedHashMap<>();

        for (CitaHora cita : citasLibres) {
            String diaSemana = cita.getHorarioAtencion().getDiaSemana(); // Obtenemos el día de la semana

            // Obtener el médico y la especialidad asociados con la cita
            Usuario medico = cita.getHorarioAtencion().getHorarioatencionusuario();
            String nombreDoctor = medico.getNombre() + " " + medico.getApellidoPaterno();
            String nombreEspecialidad = medico.getUsuarioespecialidades().getNombre();

            // Crear el DTO de la cita
            CitaHoraDTO citaDTO = citaHoraMapper.toDto(cita);
            citaDTO.setNombreDoctor(nombreDoctor);

            // Organizar la respuesta por día y especialidad
            citasPorDia.computeIfAbsent(diaSemana, k -> new LinkedHashMap<>())
                    .computeIfAbsent(nombreEspecialidad, k -> new ArrayList<>())
                    .add(citaDTO);
        }

        // Crear la estructura final de respuesta
        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("citasPorDia", citasPorDia);

        return respuesta;
    }

    // Reservar una cita específica
    public CitaHoraDTO reservarCita(int id) {
        CitaHora citaHora = citaHoraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CitaHora no encontrada con ID " + id));
        if (!"Libre".equals(citaHora.getDisponibilidad())) {
            throw new RuntimeException("La cita ya ha sido reservada");
        }
        citaHora.setDisponibilidad("Reservada");
        citaHora = citaHoraRepository.save(citaHora);
        return citaHoraMapper.toDto(citaHora);
    }

    // Actualizar citas cuando se actualiza un HorarioAtencion
    public void actualizarCitasPorHorarioAtencion(int idHorarioAtencion) {
        HorarioAtencion horarioAtencion = horarioAtencionRepository.findById(idHorarioAtencion)
                .orElseThrow(() -> new RuntimeException("HorarioAtencion no encontrado"));

        // Eliminar todas las citas antiguas asociadas con este HorarioAtencion
        citaHoraRepository.deleteByHorarioAtencionId(idHorarioAtencion);

        // Crear nuevas citas en función del horario actualizado
        createCitasByHorarioAtencion(horarioAtencion.getID());
    }

    // Crear citas basadas en un HorarioAtencion específico
    public List<CitaHoraDTO> createCitasByHorarioAtencion(int idHorarioAtencion) {
        HorarioAtencion horarioAtencion = horarioAtencionRepository.findById(idHorarioAtencion)
                .orElseThrow(() -> new RuntimeException("HorarioAtencion no encontrado"));

        List<CitaHora> citas = new ArrayList<>();
        LocalTime horaInicio = horarioAtencion.getHoraInicio();
        LocalTime horaFin = horarioAtencion.getHoraFin();
        int cantidadFichas = horarioAtencion.getCantidadFichas();

        // Calcular la duración total y ajustar con intervalos de 5 minutos entre citas
        Duration totalDuration = Duration.between(horaInicio, horaFin).minusMinutes(5 * (cantidadFichas - 1));
        Duration durationPerCita = totalDuration.dividedBy(cantidadFichas);

        for (int i = 0; i < cantidadFichas; i++) {
            LocalTime inicio = horaInicio.plus(durationPerCita.multipliedBy(i)).plusMinutes(5 * i);
            LocalTime fin = inicio.plus(durationPerCita);

            CitaHora citaHora = new CitaHora();
            citaHora.setHoraInicio(inicio);
            citaHora.setHoraFin(fin);
            citaHora.setDisponibilidad("Libre");
            citaHora.setHorarioAtencion(horarioAtencion);

            // Agregar la nueva cita a la lista
            citas.add(citaHora);
        }

        // Guardar todas las nuevas citas en la base de datos
        citaHoraRepository.saveAll(citas);

        // Convertir lista de entidades a DTOs
        return citas.stream()
                .map(citaHoraMapper::toDto)
                .collect(Collectors.toList());
    }

}