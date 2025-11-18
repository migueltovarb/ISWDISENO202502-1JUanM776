package com.garageid.repository;

import com.garageid.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
  Reservation save(Reservation r);
  Optional<Reservation> findById(String id);
  List<Reservation> findByUserId(String userId);
  List<Reservation> findBetween(LocalDateTime from, LocalDateTime to);
  void deleteById(String id);
}