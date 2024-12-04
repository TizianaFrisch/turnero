package com.ar.turnos.repository;

import com.ar.turnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TurnoRepository extends JpaRepository<Turno, Long> {
    List<Turno> findByApellido(String apellido);
    List<Turno> findByFechaAndDisponibleTrue(LocalDate fecha);
    List<Turno> findByFecha(LocalDate fecha);
    Turno findByFechaAndHora(LocalDate fecha, LocalTime hora);
    List<Turno> findByFechaLessThan(LocalDate fecha);
    List<Turno> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);
}