package com.garageid.reservation;

import com.garageid.notification.EmailService;
import com.garageid.parking.Parking;
import com.garageid.parking.ParkingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ParkingRepository parkingRepository;
    private final EmailService emailService;

    public ReservationService(ReservationRepository reservationRepository, ParkingRepository parkingRepository, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.parkingRepository = parkingRepository;
        this.emailService = emailService;
    }

    public boolean hasAvailability(String parkingId, Instant start, int durationMinutes) {
        Parking p = parkingRepository.findById(parkingId).orElse(null);
        if (p == null) return false;
        Instant end = start.plusSeconds(durationMinutes * 60L);
        List<Reservation> res = reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(parkingId, List.of(ReservationStatus.CONFIRMED, ReservationStatus.IN_USE), end, start);
        return res.size() < p.getCapacity();
    }

    public Reservation createReservation(String parkingId, String vehicleId, Instant start, int durationMinutes, String userEmail) {
        if (!hasAvailability(parkingId, start, durationMinutes)) return null;
        Reservation r = new Reservation();
        r.setParkingId(parkingId);
        r.setVehicleId(vehicleId);
        r.setStartTime(start);
        r.setDurationMinutes(durationMinutes);
        r.setStatus(ReservationStatus.CONFIRMED);
        r.setEndTime(start.plusSeconds(durationMinutes * 60L));
        r.setCode(UUID.randomUUID().toString());
        reservationRepository.save(r);
        emailService.sendReservationConfirmation(userEmail, r.getCode());
        return r;
    }
}