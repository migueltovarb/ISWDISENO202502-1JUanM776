package com.garageid.parking;

import com.garageid.integration.GeolocationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parkings")
public class ParkingController {
    private final ParkingRepository parkingRepository;
    private final GeolocationService geolocationService;

    public ParkingController(ParkingRepository parkingRepository, GeolocationService geolocationService) {
        this.parkingRepository = parkingRepository;
        this.geolocationService = geolocationService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public List<Parking> list() { return parkingRepository.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @RequestBody Parking p) {
        GeolocationService.Coordinates c = geolocationService.geocode(p.getLocation());
        p.setLatitude(c.getLat());
        p.setLongitude(c.getLng());
        parkingRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @Valid @RequestBody Parking p) {
        Parking existing = parkingRepository.findById(id).orElse(null);
        if (existing == null) return ResponseEntity.notFound().build();
        existing.setName(p.getName());
        existing.setLocation(p.getLocation());
        existing.setCapacity(p.getCapacity());
        existing.setSchedule(p.getSchedule());
        GeolocationService.Coordinates c = geolocationService.geocode(p.getLocation());
        existing.setLatitude(c.getLat());
        existing.setLongitude(c.getLng());
        parkingRepository.save(existing);
        return ResponseEntity.ok(existing);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (!parkingRepository.existsById(id)) return ResponseEntity.notFound().build();
        parkingRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}