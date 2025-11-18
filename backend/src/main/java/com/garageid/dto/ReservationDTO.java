package com.garageid.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
  private String id;
  private String vehicleId;
  private String parkingLotId;
  private LocalDateTime from;
  private LocalDateTime to;

  public ReservationDTO() {}
  public ReservationDTO(String id, String vehicleId, String parkingLotId, LocalDateTime from, LocalDateTime to) {
    this.id = id;
    this.vehicleId = vehicleId;
    this.parkingLotId = parkingLotId;
    this.from = from;
    this.to = to;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getVehicleId() { return vehicleId; }
  public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
  public String getParkingLotId() { return parkingLotId; }
  public void setParkingLotId(String parkingLotId) { this.parkingLotId = parkingLotId; }
  public LocalDateTime getFrom() { return from; }
  public void setFrom(LocalDateTime from) { this.from = from; }
  public LocalDateTime getTo() { return to; }
  public void setTo(LocalDateTime to) { this.to = to; }
}