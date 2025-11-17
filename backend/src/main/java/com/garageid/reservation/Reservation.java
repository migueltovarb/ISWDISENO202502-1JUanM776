package com.garageid.reservation;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("reservations")
public class Reservation {
    @Id
    private String id;
    private String parkingId;
    private String vehicleId;
    private Instant startTime;
    private Instant endTime;
    private int durationMinutes;
    private ReservationStatus status;
    private String code;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getParkingId() { return parkingId; }
    public void setParkingId(String parkingId) { this.parkingId = parkingId; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    public int getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(int durationMinutes) { this.durationMinutes = durationMinutes; }
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}