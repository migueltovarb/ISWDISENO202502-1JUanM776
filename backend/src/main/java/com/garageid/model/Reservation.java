package com.garageid.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
  private final String id;
  private String userId;
  private String vehicleId;
  private String parkingLotId;
  private LocalDateTime from;
  private LocalDateTime to;

  public Reservation(String userId, String vehicleId, String parkingLotId, LocalDateTime from, LocalDateTime to) {
    this.id = UUID.randomUUID().toString();
    this.userId = userId;
    this.vehicleId = vehicleId;
    this.parkingLotId = parkingLotId;
    this.from = from;
    this.to = to;
  }

  public Reservation(String id, String userId, String vehicleId, String parkingLotId, LocalDateTime from, LocalDateTime to) {
    this.id = id;
    this.userId = userId;
    this.vehicleId = vehicleId;
    this.parkingLotId = parkingLotId;
    this.from = from;
    this.to = to;
  }

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getVehicleId() { return vehicleId; }
  public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
  public String getParkingLotId() { return parkingLotId; }
  public void setParkingLotId(String parkingLotId) { this.parkingLotId = parkingLotId; }
  public LocalDateTime getFrom() { return from; }
  public void setFrom(LocalDateTime from) { this.from = from; }
  public LocalDateTime getTo() { return to; }
  public void setTo(LocalDateTime to) { this.to = to; }
}
