package com.garageid.service;

import com.garageid.dto.ReportDTO;
import com.garageid.model.Administrador;
import com.garageid.model.UsuarioBase;
import com.garageid.repository.*;
import org.springframework.stereotype.Service;

@Service
public class ReportService {
  private final UserRepository users;
  private final VehicleRepository vehicles;
  private final ReservationRepository reservations;
  private final PaymentRepository payments;

  public ReportService(UserRepository users, VehicleRepository vehicles, ReservationRepository reservations, PaymentRepository payments) {
    this.users = users; this.vehicles = vehicles; this.reservations = reservations; this.payments = payments;
  }

  public ReportDTO simpleAdminReport(String adminUserId) {
    UsuarioBase u = users.findById(adminUserId).orElseThrow();
    String title = "Reporte Garage-ID";
    int userCount = users.findAll().size();
    int vehicleCount = users.findAll().stream().mapToInt(x -> vehicles.findByUserId(x.getId()).size()).sum();
    int reservationCount = reservations.findBetween(java.time.LocalDateTime.MIN, java.time.LocalDateTime.MAX).size();
    int paymentCountForAdmin = payments.findByUserId(adminUserId).size();
    String content = "Usuarios: " + userCount + ", Vehículos: " + vehicleCount + ", Reservas: " + reservationCount + ", Pagos(admin): " + paymentCountForAdmin;
    if (u instanceof Administrador admin) content = admin.generarReporte() + " | " + content;
    return new ReportDTO(title, content);
  }
}