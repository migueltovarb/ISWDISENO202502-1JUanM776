package com.garageid.access;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("access_events")
public class AccessEvent {
    @Id
    private String id;
    private String reservationId;
    private String plate;
    private Instant entryTime;
    private Instant exitTime;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getReservationId() { return reservationId; }
    public void setReservationId(String reservationId) { this.reservationId = reservationId; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public Instant getEntryTime() { return entryTime; }
    public void setEntryTime(Instant entryTime) { this.entryTime = entryTime; }
    public Instant getExitTime() { return exitTime; }
    public void setExitTime(Instant exitTime) { this.exitTime = exitTime; }
}