package com.garageid.service;

import com.garageid.dto.ParkingLotDTO;
import com.garageid.model.ParkingLot;
import com.garageid.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingLotService {
  private final ParkingLotRepository repo;
  public ParkingLotService(ParkingLotRepository repo) { this.repo = repo; }

  public ParkingLotDTO create(String name, int capacity, String location) {
    ParkingLot p = repo.save(new ParkingLot(name, capacity, location));
    return new ParkingLotDTO(p.getId(), p.getName(), p.getCapacity(), p.getLocation());
  }

  public List<ParkingLotDTO> list() {
    return repo.findAll().stream()
        .map(p -> new ParkingLotDTO(p.getId(), p.getName(), p.getCapacity(), p.getLocation()))
        .toList();
  }

  public void delete(String id) { repo.deleteById(id); }
}