package com.garageid.access;

import com.garageid.reservation.Reservation;
import com.garageid.reservation.ReservationRepository;
import com.garageid.reservation.ReservationStatus;
import com.garageid.vehicle.Vehicle;
import com.garageid.vehicle.VehicleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/access")
public class AccessController {
    private final AccessEventRepository accessEventRepository;
    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;

    public AccessController(AccessEventRepository accessEventRepository, ReservationRepository reservationRepository, VehicleRepository vehicleRepository) {
        this.accessEventRepository = accessEventRepository;
        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PostMapping("/entry")
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> entry(@RequestParam(required = false) String reservationCode, @RequestParam(required = false) String plate) {
        Reservation r = null;
        if (reservationCode != null) {
            r = reservationRepository.findByCode(reservationCode);
        } else if (plate != null) {
            Vehicle v = vehicleRepository.findByPlate(plate).orElse(null);
            if (v != null) {
                r = reservationRepository.findTopByVehicleIdAndStatusOrderByStartTimeDesc(v.getId(), ReservationStatus.CONFIRMED);
            }
        }
        if (r == null) return ResponseEntity.badRequest().body("reservation_not_found");
        r.setStatus(ReservationStatus.IN_USE);
        reservationRepository.save(r);
        AccessEvent ev = new AccessEvent();
        ev.setReservationId(r.getId());
        ev.setPlate(plate);
        ev.setEntryTime(Instant.now());
        accessEventRepository.save(ev);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/exit")
    @PreAuthorize("hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> exit(@RequestParam(required = false) String reservationId, @RequestParam(required = false) String plate) {
        Reservation r = null;
        if (reservationId != null) {
            r = reservationRepository.findById(reservationId).orElse(null);
        } else if (plate != null) {
            Vehicle v = vehicleRepository.findByPlate(plate).orElse(null);
            if (v != null) r = reservationRepository.findTopByVehicleIdAndStatusOrderByStartTimeDesc(v.getId(), ReservationStatus.IN_USE);
        }
        if (r == null) return ResponseEntity.badRequest().body("reservation_not_found");
        r.setStatus(ReservationStatus.FINISHED);
        reservationRepository.save(r);
        AccessEvent ev = new AccessEvent();
        ev.setReservationId(r.getId());
        ev.setExitTime(Instant.now());
        accessEventRepository.save(ev);
        return ResponseEntity.ok().build();
    }
}