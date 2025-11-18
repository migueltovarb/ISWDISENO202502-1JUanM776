package com.garageid.repository.inmemory;

import com.garageid.model.Reservation;
import com.garageid.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReservationRepository implements ReservationRepository {
  private final Map<String, Reservation> reservations = new ConcurrentHashMap<>();

  @Override
  public Reservation save(Reservation r) { reservations.put(r.getId(), r); return r; }

  @Override
  public Optional<Reservation> findById(String id) { return Optional.ofNullable(reservations.get(id)); }

  @Override
  public List<Reservation> findByUserId(String userId) {
    return reservations.values().stream().filter(r -> r.getUserId().equals(userId)).toList();
  }

  @Override
  public List<Reservation> findBetween(LocalDateTime from, LocalDateTime to) {
    return reservations.values().stream().filter(r -> !r.getFrom().isAfter(to) && !r.getTo().isBefore(from)).toList();
  }

  @Override
  public void deleteById(String id) { reservations.remove(id); }
}