package com.garageid.repository;

import com.garageid.model.ParkingLot;

import java.util.List;
import java.util.Optional;

public interface ParkingLotRepository {
  ParkingLot save(ParkingLot p);
  Optional<ParkingLot> findById(String id);
  List<ParkingLot> findAll();
  void deleteById(String id);
}