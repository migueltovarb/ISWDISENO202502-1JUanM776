package com.garageid.service;

import com.garageid.dto.PaymentRequest;
import com.garageid.dto.PaymentResponse;
import com.garageid.model.Payment;
import com.garageid.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
  private final PaymentRepository payments;
  public PaymentService(PaymentRepository payments) { this.payments = payments; }

  public PaymentResponse pay(String userId, PaymentRequest req) {
    Payment p = payments.save(new Payment(userId, req.getReservationId(), req.getAmount(), "APPROVED", req.getMethod()));
    return new PaymentResponse(p.getId(), p.getStatus());
  }

  public java.util.List<com.garageid.model.Payment> history(String userId) {
    return payments.findByUserId(userId);
  }
}