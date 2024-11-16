package GestionDocumental.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import GestionDocumental.DTO.CitaHoraDTO;
import GestionDocumental.Exception.ReservaException;
import GestionDocumental.entity.Cita;
import GestionDocumental.entity.CitaHora;
import GestionDocumental.entity.HorarioAtencion;
import GestionDocumental.entity.Pacientes;
import GestionDocumental.entity.Usuario;
import GestionDocumental.repository.CitaHoraRepository;
import GestionDocumental.repository.CitaRepository;
import GestionDocumental.repository.HorarioAtencionRepository;
import GestionDocumental.repository.PacientesRepository;
import GestionDocumental.repository.UsuarioRepository;
import GestionDocumental.security.util.UsuarioDetails;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CitaService {
        private final CitaRepository citaRepository;
        private final CitaHoraRepository citaHoraRepository;
        private final UsuarioRepository usuarioRepository;
        private final HorarioAtencionRepository horarioAtencionRepository;
        private final CitaHoraService citaHoraService;
        private final PacientesRepository pacientesRepository;

        public CitaService(CitaRepository citaRepository, CitaHoraRepository citaHoraRepository,
                        UsuarioRepository usuarioRepository,
                        HorarioAtencionRepository horarioAtencionRepository,
                        CitaHoraService citaHoraService, PacientesRepository pacientesRepository) {
                this.citaRepository = citaRepository;
                this.citaHoraRepository = citaHoraRepository;
                this.usuarioRepository = usuarioRepository;
                this.horarioAtencionRepository = horarioAtencionRepository;
                this.citaHoraService = citaHoraService;
                this.pacientesRepository = pacientesRepository;
        }

        @Transactional
        public Cita registrarCita(int idCitaHora) {
                // 1. Obtener el idUsuario del paciente autenticado (a través del token)
                int idUsuarioPaciente = getAuthenticatedUserId();
                System.out.println("ID de usuario autenticado: " + idUsuarioPaciente);
                LocalDate today = LocalDate.now();
                boolean yaReservoHoy = citaRepository.existsByCitausuario_IDAndFechaCreacion(idUsuarioPaciente, today);
                if (yaReservoHoy) {
                        throw new ReservaException("Ya tienes una reserva registrada hoy. No puedes realizar otra.");
                }
                // 2. Obtener la información de CitaHora y detalles del médico a través de
                // HorarioAtencion
                CitaHoraDTO citaHoraDTO = citaHoraService.reservarCita(idCitaHora);
                System.out.println("ID de CitaHora: " + citaHoraDTO.getId());

                // 3. Obtener detalles necesarios desde CitaHora
                LocalTime horaCita = citaHoraDTO.getHoraInicio();
                int idHorarioAtencion = citaHoraDTO.getIdHorarioAtencion();
                LocalDate fechaCita = calcularFechaCita(idHorarioAtencion);
                System.out.println("Fecha de la cita calculada: " + fechaCita);

                // Validación: verificar si el paciente ya ha reservado para la fecha de
                // atención
                boolean yaReservoParaFechaCita = citaRepository.existsByCitausuario_IDAndFechaCita(idUsuarioPaciente,
                                fechaCita);
                if (yaReservoParaFechaCita) {
                        throw new ReservaException("Ya tienes una reserva registrada. No puedes realizar otra.");
                }
                // 4. Crear y configurar la entidad Cita
                Cita cita = new Cita();
                cita.setFechaCita(fechaCita);
                cita.setHoraCita(horaCita);
                cita.setFechaCreacion(LocalDate.now());
                cita.setEstado("Reservada");

                // 5. Asignar relaciones necesarias para guardar en la base de datos
                Usuario paciente = usuarioRepository.findById(idUsuarioPaciente)
                                .orElseThrow(() -> new RuntimeException(
                                                "Usuario no encontrado con ID: " + idUsuarioPaciente));
                CitaHora citaHora = citaHoraRepository.findById(idCitaHora)
                                .orElseThrow(() -> new RuntimeException(
                                                "CitaHora no encontrada con ID: " + idCitaHora));

                cita.setCitausuario(paciente); // Guardamos el paciente como el usuario
                cita.setCitaHora(citaHora); // Relacionamos con CitaHora para obtener médico y horario

                // 6. Guardar la cita en la base de datos
                citaRepository.save(cita);
                System.out.println("Cita guardada con ID: " + cita.getID());

                // Retornar la entidad cita sin detalles adicionales
                return cita;
        }

        // Método auxiliar para calcular la fecha de la cita basado en el día de la
        // semana
        private LocalDate calcularFechaCita(int idHorarioAtencion) {
                HorarioAtencion horarioAtencion = horarioAtencionRepository.findById(idHorarioAtencion)
                                .orElseThrow(() -> new RuntimeException(
                                                "Horario de atención no encontrado con ID: " + idHorarioAtencion));

                DayOfWeek diaSemana = convertirDiaSemana(horarioAtencion.getDiaSemana());
                LocalDate fechaActual = LocalDate.now();

                while (fechaActual.getDayOfWeek() != diaSemana) {
                        fechaActual = fechaActual.plusDays(1);
                }
                return fechaActual;
        }

        // Método auxiliar para obtener el ID del usuario autenticado desde el contexto
        // de seguridad
        private int getAuthenticatedUserId() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UsuarioDetails userDetails = (UsuarioDetails) authentication.getPrincipal();
                return userDetails.getUsuario().getID();
        }

        private DayOfWeek convertirDiaSemana(String diaSemana) {
                switch (diaSemana.toUpperCase()) {
                        case "LUNES":
                                return DayOfWeek.MONDAY;
                        case "MARTES":
                                return DayOfWeek.TUESDAY;
                        case "MIERCOLES":
                                return DayOfWeek.WEDNESDAY;
                        case "JUEVES":
                                return DayOfWeek.THURSDAY;
                        case "VIERNES":
                                return DayOfWeek.FRIDAY;
                        case "SABADO":
                                return DayOfWeek.SATURDAY;
                        case "DOMINGO":
                                return DayOfWeek.SUNDAY;
                        default:
                                throw new IllegalArgumentException("Día de la semana no válido: " + diaSemana);
                }
        }

        public Cita obtenerCitaPorHistorial(int historialMedicoId) {
                // Buscar el paciente en base al historial médico
                Pacientes paciente = pacientesRepository.findById(historialMedicoId)
                                .orElseThrow(() -> new EntityNotFoundException("Historial médico no encontrado"));

                // Buscar la última cita en estado "En Proceso" de este paciente
                return citaRepository
                                .findTopByCitausuarioAndEstadoOrderByFechaCitaDesc(paciente.getPacienteusuario(),
                                                "En Proceso")
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "No hay citas en proceso para este paciente."));
        }

        public Optional<Cita> obtenerCitaPorId(int id) {
                return citaRepository.findById(id);
        }

}
