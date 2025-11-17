package com.garageid.parking;

import com.garageid.integration.GeolocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParkingControllerTest {
    private ParkingRepository parkingRepository;
    private GeolocationService geolocationService;
    private ParkingController parkingController;

    @BeforeEach
    void setup() {
        parkingRepository = mock(ParkingRepository.class);
        geolocationService = mock(GeolocationService.class);
        parkingController = new ParkingController(parkingRepository, geolocationService);
    }

    @Test
    void testList() {
        when(parkingRepository.findAll()).thenReturn(List.of(new Parking()));
        List<Parking> res = parkingController.list();
        assertEquals(1, res.size());
        verify(parkingRepository, times(1)).findAll();
    }

    @Test
    void testCreateSetsCoordinates() {
        GeolocationService.Coordinates c = new GeolocationService.Coordinates(4.7, -74.0);
        when(geolocationService.geocode("Bogota")).thenReturn(c);
        Parking p = new Parking();
        p.setName("P1");
        p.setLocation("Bogota");
        p.setCapacity(100);
        p.setSchedule("24/7");
        parkingController.create(p);
        ArgumentCaptor<Parking> captor = ArgumentCaptor.forClass(Parking.class);
        verify(parkingRepository).save(captor.capture());
        assertEquals(4.7, captor.getValue().getLatitude());
        assertEquals(-74.0, captor.getValue().getLongitude());
    }

    @Test
    void testUpdate() {
        Parking existing = new Parking();
        existing.setId("PID");
        when(parkingRepository.findById("PID")).thenReturn(Optional.of(existing));
        GeolocationService.Coordinates c = new GeolocationService.Coordinates(1.0, 2.0);
        when(geolocationService.geocode("New Address")).thenReturn(c);
        Parking p = new Parking();
        p.setName("NewName");
        p.setLocation("New Address");
        p.setCapacity(50);
        p.setSchedule("8-18");
        parkingController.update("PID", p);
        ArgumentCaptor<Parking> captor = ArgumentCaptor.forClass(Parking.class);
        verify(parkingRepository).save(captor.capture());
        assertEquals("NewName", captor.getValue().getName());
        assertEquals(1.0, captor.getValue().getLatitude());
        assertEquals(2.0, captor.getValue().getLongitude());
    }

    @Test
    void testDeleteNotFound() {
        when(parkingRepository.existsById("X")).thenReturn(false);
        var res = parkingController.delete("X");
        assertEquals(404, res.getStatusCode().value());
    }
}