package com.garageid.controller;

import com.garageid.dto.VehicleDTO;
import com.garageid.service.VehicleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {
  private final VehicleService service;
  public VehicleController(VehicleService service) { this.service = service; }

  @PostMapping
  public VehicleDTO add(Authentication auth, @RequestBody Map<String, String> body) {
    String userId = auth.getName();
    String type = body.getOrDefault("type", "CARRO");
    String color = body.getOrDefault("color", "");
    return service.add(userId, body.get("plate"), body.get("brand"), body.get("model"), type, color);
  }

  @GetMapping("/my")
  public List<VehicleDTO> my(Authentication auth) {
    return service.listMy(auth.getName());
  }

  @PutMapping("/{id}")
  public VehicleDTO update(@PathVariable String id, @RequestBody Map<String, String> body) {
    return service.update(id,
        body.get("plate"),
        body.get("brand"),
        body.get("model"),
        body.get("type"),
        body.get("color"));
  }

  @DeleteMapping("/{id}")
  public Map<String, String> delete(@PathVariable String id) {
    service.delete(id);
    return Map.of("status", "DELETED");
  }
}