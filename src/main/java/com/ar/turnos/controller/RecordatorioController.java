package com.ar.turnos.controller;

import com.ar.turnos.service.RecordatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecordatorioController {

    @Autowired
    private RecordatorioService recordatorioService;

    // Endpoint para probar el envío de recordatorios manualmente
    @GetMapping("/recordatorios/enviar")
    public String enviarRecordatorios() {
        try {
            recordatorioService.enviarRecordatorios();
            return "Recordatorios enviados correctamente.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error al enviar los recordatorios: " + e.getMessage();
        }
    }
}
