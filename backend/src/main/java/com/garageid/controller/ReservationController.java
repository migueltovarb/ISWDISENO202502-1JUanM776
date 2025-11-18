package com.garageid.controller;

import com.garageid.dto.ReservationDTO;
import com.garageid.service.ReservationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {
  private final ReservationService service;
  public ReservationController(ReservationService service) { this.service = service; }

  @PostMapping
  public ReservationDTO create(Authentication auth, @RequestBody Map<String, String> body) {
    String userId = auth.getName();
    return service.create(
        userId,
        body.get("vehicleId"),
        body.get("parkingLotId"),
        LocalDateTime.parse(body.get("from")),
        LocalDateTime.parse(body.get("to"))
    );
  }

  @GetMapping("/my")
  public List<ReservationDTO> my(Authentication auth) { return service.myReservations(auth.getName()); }

  @PutMapping("/{id}")
  public ReservationDTO update(@PathVariable String id, @RequestBody Map<String, String> body) {
    return service.update(id,
        body.get("from")!=null ? LocalDateTime.parse(body.get("from")) : null,
        body.get("to")!=null ? LocalDateTime.parse(body.get("to")) : null);
  }

  @DeleteMapping("/{id}")
  public Map<String, String> delete(@PathVariable String id) {
    service.delete(id);
    return Map.of("status", "DELETED");
  }
}