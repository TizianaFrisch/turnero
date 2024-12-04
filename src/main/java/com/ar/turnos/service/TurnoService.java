package com.ar.turnos.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.ar.turnos.model.Turno;
import com.ar.turnos.repository.TurnoRepository;

@Service
public class TurnoService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Scheduled(cron = "0 0 10 * * ?") // Ejecuta la tarea todos los días a las 10:00 AM
    public void enviarRecordatorios() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Turno> turnos = turnoRepository.findByFecha(tomorrow);
        // Implementar lógica de envío de recordatorio
        // Por ejemplo, enviar emails o notificaciones
    }

    public boolean verificarDisponibilidad(LocalDate fecha, LocalTime hora) {
        Turno turnoExistente = turnoRepository.findByFechaAndHora(fecha, hora);
        return turnoExistente == null;
    }

    public List<Turno> obtenerTurnosPorApellido(String apellido) {
        return turnoRepository.findByApellido(apellido);
    }

    public List<Turno> obtenerTurnosDesde(LocalDate fecha) {
        return turnoRepository.findByFechaGreaterThanEqualOrderByFechaAsc(fecha);
    }

    public Turno obtenerTurnoPorId(Long id) {
        return turnoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Turno no encontrado"));
    }

    public Turno guardarTurno(Turno turno) {
        return turnoRepository.save(turno);
    }

    public void eliminarTurno(Long id) {
        turnoRepository.deleteById(id);
    }
    public List<LocalTime> obtenerHorariosDisponibles(LocalDate fecha) {
        // Lista de horarios disponibles (por ejemplo)
        List<LocalTime> horariosDisponibles = Arrays.asList(
            LocalTime.of(9, 0),
            LocalTime.of(10, 0),
            LocalTime.of(11, 0),
            LocalTime.of(12, 0),
            LocalTime.of(13, 0),
            LocalTime.of(14, 0),
            LocalTime.of(15, 0),
            LocalTime.of(16, 0),
            LocalTime.of(17, 0)
        );
        
        // Filtrar horarios ya ocupados
        List<Turno> turnosExistentes = turnoRepository.findByFecha(fecha);
        
        return horariosDisponibles.stream()
            .filter(horario -> 
                turnosExistentes.stream()
                .noneMatch(turno -> turno.getHora().equals(horario)))
            .collect(Collectors.toList());
    }

    // Método para eliminar turnos antiguos
    public void eliminarTurnosAntesDe(LocalDate fecha) {
        List<Turno> turnosAntiguos = turnoRepository.findByFechaLessThan(fecha);
        turnoRepository.deleteAll(turnosAntiguos);
    }
}