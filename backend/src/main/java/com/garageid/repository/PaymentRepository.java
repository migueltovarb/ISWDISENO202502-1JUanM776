package com.garageid.repository;

import com.garageid.model.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
  Payment save(Payment p);
  Optional<Payment> findById(String id);
  List<Payment> findByUserId(String userId);
}