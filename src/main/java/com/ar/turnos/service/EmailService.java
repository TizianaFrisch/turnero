package com.ar.turnos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;

    
    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);  // Usa el correo configurado en application.properties
        message.setTo(destinatario);
        message.setSubject(asunto);
        message.setText(cuerpo);
        
        emailSender.send(message);
    }

    // Método para enviar un recordatorio un día antes del turno
    public void enviarRecordatorio(String email, String fechaTurno, String horaTurno) {
        String asunto = "Recordatorio de Turno Médico";
        String cuerpo = "Este es un recordatorio de su turno médico reservado para el " + fechaTurno + " a las " + horaTurno + "con la DRA CampoMar";
        
        enviarCorreo(email, asunto, cuerpo);
    }
}
