package com.garageid.vehicle;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("vehicles")
@CompoundIndexes({
        @CompoundIndex(name = "owner_plate_unique", def = "{ 'ownerUserId': 1, 'plate': 1 }", unique = true)
})
public class Vehicle {
    @Id
    private String id;
    @Indexed
    private String plate;
    private String type;
    private String brand;
    private String model;
    private String color;
    private String ownerUserId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPlate() { return plate; }
    public void setPlate(String plate) { this.plate = plate; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public String getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(String ownerUserId) { this.ownerUserId = ownerUserId; }
}