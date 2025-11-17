package com.garageid.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class CreateReservationRequest {
    @NotBlank
    private String parkingId;
    @NotBlank
    private String vehicleId;
    @NotNull
    private Instant startTime;
    @NotNull
    private Integer durationMinutes;

    public String getParkingId() { return parkingId; }
    public void setParkingId(String parkingId) { this.parkingId = parkingId; }
    public String getVehicleId() { return vehicleId; }
    public void setVehicleId(String vehicleId) { this.vehicleId = vehicleId; }
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
}