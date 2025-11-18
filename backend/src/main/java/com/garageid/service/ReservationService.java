package com.garageid.service;

import com.garageid.dto.ReservationDTO;
import com.garageid.model.Reservation;
import com.garageid.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
  private final ReservationRepository repo;
  public ReservationService(ReservationRepository repo) { this.repo = repo; }

  public ReservationDTO create(String userId, String vehicleId, String parkingLotId, LocalDateTime from, LocalDateTime to) {
    Reservation r = repo.save(new Reservation(userId, vehicleId, parkingLotId, from, to));
    return new ReservationDTO(r.getId(), r.getVehicleId(), r.getParkingLotId(), r.getFrom(), r.getTo());
  }

  public List<ReservationDTO> myReservations(String userId) {
    return repo.findByUserId(userId).stream()
        .map(r -> new ReservationDTO(r.getId(), r.getVehicleId(), r.getParkingLotId(), r.getFrom(), r.getTo()))
        .toList();
  }

  public ReservationDTO update(String id, LocalDateTime from, LocalDateTime to) {
    Reservation r = repo.findById(id).orElseThrow();
    if (from != null) r.setFrom(from);
    if (to != null) r.setTo(to);
    repo.save(r);
    return new ReservationDTO(r.getId(), r.getVehicleId(), r.getParkingLotId(), r.getFrom(), r.getTo());
  }

  public void delete(String id) { repo.deleteById(id); }
}