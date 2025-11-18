package com.garageid.repository;

import com.garageid.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {
  Vehicle save(Vehicle v);
  Optional<Vehicle> findById(String id);
  List<Vehicle> findByUserId(String userId);
  void deleteById(String id);
}