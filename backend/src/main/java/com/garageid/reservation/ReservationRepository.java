package com.garageid.reservation;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
    List<Reservation> findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(String parkingId, List<ReservationStatus> statuses, Instant end, Instant start);
    Reservation findByCode(String code);
    Reservation findTopByVehicleIdAndStatusOrderByStartTimeDesc(String vehicleId, ReservationStatus status);
}