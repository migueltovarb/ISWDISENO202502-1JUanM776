package com.garageid.notification;

public interface EmailService {
    void sendReservationConfirmation(String toEmail, String reservationCode);
    void sendPasswordReset(String toEmail, String resetToken);
}