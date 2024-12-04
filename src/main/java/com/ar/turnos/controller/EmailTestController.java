package com.ar.turnos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.turnos.service.EmailService;

@RestController
public class EmailTestController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    public String enviarCorreoDePrueba() {
        // Simula el envío de un correo de prueba
        String emailDestino = "frischtiziana@gmail.com"; // Reemplaza con un correo real para pruebas
        String asunto = "Correo de Prueba";
        String cuerpo = "Este es un correo de prueba desde la aplicación de turnos.";

        emailService.enviarCorreo(emailDestino, asunto, cuerpo);
        
        return "Correo enviado correctamente a " + emailDestino;
    }
}
