package com.garageid.model;

import java.util.UUID;

public class Vehicle {
  private final String id;
  private String userId;
  private String plate;
  private String brand;
  private String model;
  private String type;
  private String color;

  public Vehicle(String userId, String plate, String brand, String model, String type, String color) {
    this.id = UUID.randomUUID().toString();
    this.userId = userId;
    this.plate = plate;
    this.brand = brand;
    this.model = model;
    this.type = type;
    this.color = color;
  }

  public String getId() { return id; }
  public String getUserId() { return userId; }
  public void setUserId(String userId) { this.userId = userId; }
  public String getPlate() { return plate; }
  public void setPlate(String plate) { this.plate = plate; }
  public String getBrand() { return brand; }
  public void setBrand(String brand) { this.brand = brand; }
  public String getModel() { return model; }
  public void setModel(String model) { this.model = model; }
  public String getType() { return type; }
  public void setType(String type) { this.type = type; }
  public String getColor() { return color; }
  public void setColor(String color) { this.color = color; }
}