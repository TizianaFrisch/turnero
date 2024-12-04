package com.ar.turnos.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class TurnoCleanupService {
    private final TurnoService turnoService;

    public TurnoCleanupService(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    // Método para eliminar turnos antiguos
    @Scheduled(cron = "0 0 0 * * ?")  // Cada día a medianoche
    public void eliminarTurnosAntiguos() {
        LocalDate hoy = LocalDate.now();
        turnoService.eliminarTurnosAntesDe(hoy); 
        System.out.println("Turnos antiguos eliminados.");
    }
}