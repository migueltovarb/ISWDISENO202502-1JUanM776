package com.garageid.reservation;

import com.garageid.notification.EmailService;
import com.garageid.parking.Parking;
import com.garageid.parking.ParkingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ReservationServiceTest {
    private ReservationRepository reservationRepository;
    private ParkingRepository parkingRepository;
    private EmailService emailService;
    private ReservationService reservationService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        reservationRepository = Mockito.mock(ReservationRepository.class);
        parkingRepository = Mockito.mock(ParkingRepository.class);
        emailService = Mockito.mock(EmailService.class);
        reservationService = new ReservationService(reservationRepository, parkingRepository, emailService);
    }

    @Test
    void testHasAvailabilityWithOverlap() {
        String parkingId = "P1";
        Parking p = new Parking();
        p.setId(parkingId);
        p.setCapacity(2);
        when(parkingRepository.findById(parkingId)).thenReturn(java.util.Optional.of(p));

        Instant start = Instant.now();
        int duration = 60;
        Instant end = start.plusSeconds(duration * 60L);

        when(reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(eq(parkingId), anyList(), eq(end), eq(start)))
                .thenReturn(List.of(new Reservation()));

        assertTrue(reservationService.hasAvailability(parkingId, start, duration));

        when(reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(eq(parkingId), anyList(), eq(end), eq(start)))
                .thenReturn(List.of(new Reservation(), new Reservation()));

        assertFalse(reservationService.hasAvailability(parkingId, start, duration));
    }

    @Test
    void testCreateReservationSetsEndSendsEmail() {
        String parkingId = "P1";
        Parking p = new Parking();
        p.setId(parkingId);
        p.setCapacity(5);
        when(parkingRepository.findById(parkingId)).thenReturn(java.util.Optional.of(p));

        Instant start = Instant.now();
        int duration = 30;
        when(reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(eq(parkingId), anyList(), any(), any()))
                .thenReturn(List.of());

        Reservation r = reservationService.createReservation(parkingId, "V1", start, duration, "user@example.com");
        assertNotNull(r);
        assertEquals(ReservationStatus.CONFIRMED, r.getStatus());
        assertEquals(start.plusSeconds(duration * 60L), r.getEndTime());
        verify(reservationRepository, times(1)).save(any());
        verify(emailService, times(1)).sendReservationConfirmation(eq("user@example.com"), anyString());
    }
}