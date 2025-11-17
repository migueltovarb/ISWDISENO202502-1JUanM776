package com.garageid.parking;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ParkingRepository extends MongoRepository<Parking, String> {
}