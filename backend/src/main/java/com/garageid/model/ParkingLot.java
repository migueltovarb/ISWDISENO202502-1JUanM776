package com.garageid.model;

import java.util.UUID;

public class ParkingLot {
  private final String id;
  private String name;
  private int capacity;
  private String location;

  public ParkingLot(String name, int capacity, String location) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.capacity = capacity;
    this.location = location;
  }

  public String getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public int getCapacity() { return capacity; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }
}