package com.garageid.service;

import com.garageid.dto.VehicleDTO;
import com.garageid.model.Vehicle;
import com.garageid.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
  private final VehicleRepository vehicles;
  public VehicleService(VehicleRepository vehicles) { this.vehicles = vehicles; }

  public VehicleDTO add(String userId, String plate, String brand, String model, String type, String color) {
    Vehicle v = vehicles.save(new Vehicle(userId, plate, brand, model, type, color));
    return new VehicleDTO(v.getId(), v.getPlate(), v.getBrand(), v.getModel(), v.getType(), v.getColor());
  }

  public List<VehicleDTO> listMy(String userId) {
    return vehicles.findByUserId(userId).stream()
        .map(v -> new VehicleDTO(v.getId(), v.getPlate(), v.getBrand(), v.getModel(), v.getType(), v.getColor()))
        .toList();
  }

  public VehicleDTO update(String id, String plate, String brand, String model, String type, String color) {
    Vehicle v = vehicles.findById(id).orElseThrow();
    if (plate != null) v.setPlate(plate);
    if (brand != null) v.setBrand(brand);
    if (model != null) v.setModel(model);
    if (type != null) v.setType(type);
    if (color != null) v.setColor(color);
    vehicles.save(v);
    return new VehicleDTO(v.getId(), v.getPlate(), v.getBrand(), v.getModel(), v.getType(), v.getColor());
  }

  public void delete(String id) { vehicles.deleteById(id); }
}