package com.garageid.controller;

import com.garageid.dto.PaymentRequest;
import com.garageid.dto.PaymentResponse;
import com.garageid.service.PaymentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@CrossOrigin(origins = "*")
public class PaymentController {
  private final PaymentService service;
  public PaymentController(PaymentService service) { this.service = service; }

  @PostMapping("/pay")
  public PaymentResponse pay(Authentication auth, @RequestBody PaymentRequest req) {
    return service.pay(auth.getName(), req);
  }

  @GetMapping("/history")
  public java.util.List<java.util.Map<String, Object>> history(Authentication auth) {
    var list = service.history(auth.getName());
    return list.stream().map(p -> {
      java.util.Map<String, Object> m = new java.util.LinkedHashMap<>();
      m.put("id", p.getId());
      m.put("reservationId", p.getReservationId());
      m.put("amount", p.getAmount());
      m.put("method", p.getMethod());
      m.put("status", p.getStatus());
      m.put("createdAt", p.getCreatedAt().toString());
      return m;
    }).toList();
  }
}