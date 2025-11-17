package com.garageid.vehicle;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends MongoRepository<Vehicle, String> {
    List<Vehicle> findByOwnerUserId(String ownerUserId);
    Optional<Vehicle> findByPlateAndOwnerUserId(String plate, String ownerUserId);
    Optional<Vehicle> findByPlate(String plate);
}