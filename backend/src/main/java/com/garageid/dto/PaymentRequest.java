package com.garageid.dto;

public class PaymentRequest {
  private String reservationId;
  private double amount;
  private String method;

  public String getReservationId() { return reservationId; }
  public void setReservationId(String reservationId) { this.reservationId = reservationId; }
  public double getAmount() { return amount; }
  public void setAmount(double amount) { this.amount = amount; }
  public String getMethod() { return method; }
  public void setMethod(String method) { this.method = method; }
}