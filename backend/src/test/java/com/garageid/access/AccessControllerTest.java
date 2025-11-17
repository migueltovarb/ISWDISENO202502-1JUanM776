package com.garageid.access;

import com.garageid.reservation.Reservation;
import com.garageid.reservation.ReservationRepository;
import com.garageid.reservation.ReservationStatus;
import com.garageid.vehicle.Vehicle;
import com.garageid.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccessControllerTest {
    private AccessEventRepository accessEventRepository;
    private ReservationRepository reservationRepository;
    private VehicleRepository vehicleRepository;
    private AccessController accessController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        accessEventRepository = Mockito.mock(AccessEventRepository.class);
        reservationRepository = Mockito.mock(ReservationRepository.class);
        vehicleRepository = Mockito.mock(VehicleRepository.class);
        accessController = new AccessController(accessEventRepository, reservationRepository, vehicleRepository);
    }

    @Test
    void testEntryByPlateTransitionsToInUse() {
        Vehicle v = new Vehicle();
        v.setId("V1");
        v.setPlate("ABC123");
        when(vehicleRepository.findByPlate("ABC123")).thenReturn(java.util.Optional.of(v));

        Reservation r = new Reservation();
        r.setId("R1");
        r.setStatus(ReservationStatus.CONFIRMED);
        when(reservationRepository.findTopByVehicleIdAndStatusOrderByStartTimeDesc("V1", ReservationStatus.CONFIRMED)).thenReturn(r);

        ResponseEntity<?> res = accessController.entry(null, "ABC123");
        assertEquals(200, res.getStatusCode().value());

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());
        assertEquals(ReservationStatus.IN_USE, captor.getValue().getStatus());
    }

    @Test
    void testExitByPlateTransitionsToFinished() {
        Vehicle v = new Vehicle();
        v.setId("V1");
        v.setPlate("ABC123");
        when(vehicleRepository.findByPlate("ABC123")).thenReturn(java.util.Optional.of(v));

        Reservation r = new Reservation();
        r.setId("R1");
        r.setStatus(ReservationStatus.IN_USE);
        when(reservationRepository.findTopByVehicleIdAndStatusOrderByStartTimeDesc("V1", ReservationStatus.IN_USE)).thenReturn(r);

        ResponseEntity<?> res = accessController.exit(null, "ABC123");
        assertEquals(200, res.getStatusCode().value());

        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());
        assertEquals(ReservationStatus.FINISHED, captor.getValue().getStatus());
    }
}