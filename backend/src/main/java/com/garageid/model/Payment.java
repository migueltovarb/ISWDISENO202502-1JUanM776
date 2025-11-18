package com.garageid.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
  private final String id;
  private final String userId;
  private final String reservationId;
  private final double amount;
  private final LocalDateTime createdAt;
  private String status;
  private final String method;

  public Payment(String userId, String reservationId, double amount, String status, String method) {
    this.id = UUID.randomUUID().toString();
    this.userId = userId;
    this.reservationId = reservationId;
    this.amount = amount;
    this.status = status;
    this.createdAt = LocalDateTime.now();
    this.method = method;
  }

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public String getReservationId() { return reservationId; }
  public double getAmount() { return amount; }
  public LocalDateTime getCreatedAt() { return createdAt; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public String getMethod() { return method; }
}