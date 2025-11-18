package com.garageid.dto;

public class VehicleDTO {
  private String id;
  private String plate;
  private String brand;
  private String model;
  private String type;
  private String color;

  public VehicleDTO() {}
  public VehicleDTO(String id, String plate, String brand, String model, String type, String color) {
    this.id = id;
    this.plate = plate;
    this.brand = brand;
    this.model = model;
    this.type = type;
    this.color = color;
  }

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
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