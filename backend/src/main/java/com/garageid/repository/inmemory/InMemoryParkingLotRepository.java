package com.garageid.repository.inmemory;

import com.garageid.model.ParkingLot;
import com.garageid.repository.ParkingLotRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryParkingLotRepository implements ParkingLotRepository {
  private final Map<String, ParkingLot> lots = new ConcurrentHashMap<>();

  @Override
  public ParkingLot save(ParkingLot p) { lots.put(p.getId(), p); return p; }

  @Override
  public Optional<ParkingLot> findById(String id) { return Optional.ofNullable(lots.get(id)); }

  @Override
  public List<ParkingLot> findAll() { return new ArrayList<>(lots.values()); }

  @Override
  public void deleteById(String id) { lots.remove(id); }
}