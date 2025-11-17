package com.garageid.notification;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SimpleEmailService implements EmailService {
    private final JavaMailSender mailSender;

    public SimpleEmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendReservationConfirmation(String toEmail, String reservationCode) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Confirmación de reserva");
        msg.setText("Código de reserva: " + reservationCode);
        mailSender.send(msg);
    }

    @Override
    public void sendPasswordReset(String toEmail, String resetToken) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Recuperación de contraseña");
        msg.setText("Token de recuperación: " + resetToken);
        mailSender.send(msg);
    }
}