package com.garageid.repository.inmemory;

import com.garageid.model.Vehicle;
import com.garageid.repository.VehicleRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryVehicleRepository implements VehicleRepository {
  private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();

  @Override
  public Vehicle save(Vehicle v) { vehicles.put(v.getId(), v); return v; }

  @Override
  public Optional<Vehicle> findById(String id) { return Optional.ofNullable(vehicles.get(id)); }

  @Override
  public List<Vehicle> findByUserId(String userId) {
    return vehicles.values().stream().filter(v -> v.getUserId().equals(userId)).toList();
  }

  @Override
  public void deleteById(String id) { vehicles.remove(id); }
}