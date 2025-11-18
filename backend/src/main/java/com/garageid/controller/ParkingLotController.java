package com.garageid.controller;

import com.garageid.dto.ParkingLotDTO;
import com.garageid.service.ParkingLotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/parking")
@CrossOrigin(origins = "*")
public class ParkingLotController {
  private final ParkingLotService service;
  public ParkingLotController(ParkingLotService service) { this.service = service; }

  @PostMapping
  public ParkingLotDTO create(@RequestBody Map<String, String> body) {
    return service.create(body.get("name"), Integer.parseInt(body.get("capacity")), body.get("location"));
  }

  @GetMapping
  public List<ParkingLotDTO> list() { return service.list(); }

  @DeleteMapping("/{id}")
  public java.util.Map<String, String> delete(@PathVariable String id) {
    service.delete(id);
    return java.util.Map.of("status", "DELETED");
  }
}