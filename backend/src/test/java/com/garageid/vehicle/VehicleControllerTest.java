package com.garageid.vehicle;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VehicleControllerTest {
    private VehicleRepository vehicleRepository;
    private VehicleController vehicleController;
    private Authentication auth;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        vehicleRepository = mock(VehicleRepository.class);
        vehicleController = new VehicleController(vehicleRepository);
        auth = new MinimalAuth("user@example.com");
    }

    @Test
    void testListByOwner() {
        when(vehicleRepository.findByOwnerUserId("user@example.com")).thenReturn(List.of(new Vehicle()));
        List<Vehicle> res = vehicleController.list(auth);
        assertEquals(1, res.size());
        verify(vehicleRepository, times(1)).findByOwnerUserId("user@example.com");
    }

    @Test
    void testCreateDuplicatePlateFails() {
        Vehicle v = new Vehicle();
        v.setPlate("ABC123");
        when(vehicleRepository.findByPlateAndOwnerUserId("ABC123", "user@example.com")).thenReturn(Optional.of(new Vehicle()));
        ResponseEntity<?> res = vehicleController.create(auth, v);
        assertEquals(400, res.getStatusCode().value());
    }

    @Test
    void testCreateSetsOwnerAndSaves() {
        Vehicle v = new Vehicle();
        v.setPlate("XYZ987");
        when(vehicleRepository.findByPlateAndOwnerUserId("XYZ987", "user@example.com")).thenReturn(Optional.empty());
        ResponseEntity<?> res = vehicleController.create(auth, v);
        assertEquals(200, res.getStatusCode().value());
        ArgumentCaptor<Vehicle> captor = ArgumentCaptor.forClass(Vehicle.class);
        verify(vehicleRepository).save(captor.capture());
        assertEquals("user@example.com", captor.getValue().getOwnerUserId());
    }

    @Test
    void testUpdateWrongOwnerNotFound() {
        Vehicle existing = new Vehicle();
        existing.setId("V1");
        existing.setOwnerUserId("other@example.com");
        when(vehicleRepository.findById("V1")).thenReturn(Optional.of(existing));
        Vehicle update = new Vehicle();
        ResponseEntity<?> res = vehicleController.update(auth, "V1", update);
        assertEquals(404, res.getStatusCode().value());
    }

    static class MinimalAuth implements Authentication {
        private final String name;
        MinimalAuth(String name) { this.name = name; }
        @Override public String getName() { return name; }
        @Override public Object getCredentials() { return null; }
        @Override public Object getDetails() { return null; }
        @Override public Object getPrincipal() { return null; }
        @Override public boolean isAuthenticated() { return true; }
        @Override public void setAuthenticated(boolean isAuthenticated) {}
        @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() { return java.util.Collections.emptyList(); }
    }
}