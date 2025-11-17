package com.garageid.reservation;

import com.garageid.reservation.dto.CreateReservationRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    public ReservationController(ReservationRepository reservationRepository, ReservationService reservationService) {
        this.reservationRepository = reservationRepository;
        this.reservationService = reservationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public List<Reservation> list() { return reservationRepository.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(Authentication auth, @Valid @RequestBody CreateReservationRequest req) {
        Reservation r = reservationService.createReservation(req.getParkingId(), req.getVehicleId(), req.getStartTime(), req.getDurationMinutes(), auth.getName());
        if (r == null) return ResponseEntity.badRequest().body("no_capacity");
        return ResponseEntity.ok(r);
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> cancel(@PathVariable String id) {
        Reservation r = reservationRepository.findById(id).orElse(null);
        if (r == null) return ResponseEntity.notFound().build();
        r.setStatus(ReservationStatus.CANCELED);
        reservationRepository.save(r);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/availability")
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> availability(@RequestParam String parkingId, @RequestParam Instant startTime, @RequestParam int durationMinutes) {
        boolean ok = reservationService.hasAvailability(parkingId, startTime, durationMinutes);
        return ResponseEntity.ok(ok);
    }
}