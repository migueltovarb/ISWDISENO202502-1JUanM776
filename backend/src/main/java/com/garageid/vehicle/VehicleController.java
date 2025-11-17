package com.garageid.vehicle;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleRepository vehicleRepository;

    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public List<Vehicle> list(Authentication auth) {
        return vehicleRepository.findByOwnerUserId(auth.getName());
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> create(Authentication auth, @Valid @RequestBody Vehicle v) {
        if (vehicleRepository.findByPlateAndOwnerUserId(v.getPlate(), auth.getName()).isPresent()) return ResponseEntity.badRequest().body("plate_in_use");
        v.setOwnerUserId(auth.getName());
        vehicleRepository.save(v);
        return ResponseEntity.ok(v);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> update(Authentication auth, @PathVariable String id, @Valid @RequestBody Vehicle v) {
        Vehicle existing = vehicleRepository.findById(id).orElse(null);
        if (existing == null || !auth.getName().equals(existing.getOwnerUserId())) return ResponseEntity.notFound().build();
        if (!existing.getPlate().equals(v.getPlate()) && vehicleRepository.findByPlateAndOwnerUserId(v.getPlate(), auth.getName()).isPresent()) return ResponseEntity.badRequest().body("plate_in_use");
        existing.setPlate(v.getPlate());
        existing.setType(v.getType());
        existing.setBrand(v.getBrand());
        existing.setModel(v.getModel());
        existing.setColor(v.getColor());
        vehicleRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(Authentication auth, @PathVariable String id) {
        Vehicle existing = vehicleRepository.findById(id).orElse(null);
        if (existing == null || !auth.getName().equals(existing.getOwnerUserId())) return ResponseEntity.notFound().build();
        vehicleRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}