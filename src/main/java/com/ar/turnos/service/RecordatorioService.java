package com.ar.turnos.service;

import com.ar.turnos.model.Turno;
import com.ar.turnos.repository.TurnoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecordatorioService {

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private EmailService emailService;

    // Programar recordatorios automáticos a las 10:00 AM todos los días
    @Scheduled(cron = "0 0 10 * * ?")  // Cambiar hora si es necesario
    public void enviarRecordatorios() {
        LocalDate tomorrow = LocalDate.now().plusDays(1); // Fecha del día siguiente
        List<Turno> turnos = turnoRepository.findByFecha(tomorrow);

        for (Turno turno : turnos) {
            emailService.enviarRecordatorio(turno.getEmail(), 
                                            turno.getFecha().toString(), 
                                            turno.getHora().toString());
            System.out.println("Recordatorio enviado a: " + turno.getEmail());
        }
    }
}
