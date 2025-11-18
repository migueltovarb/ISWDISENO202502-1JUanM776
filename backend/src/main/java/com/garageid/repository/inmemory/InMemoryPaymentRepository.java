package com.garageid.repository.inmemory;

import com.garageid.model.Payment;
import com.garageid.repository.PaymentRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryPaymentRepository implements PaymentRepository {
  private final Map<String, Payment> payments = new ConcurrentHashMap<>();

  @Override
  public Payment save(Payment p) { payments.put(p.getId(), p); return p; }

  @Override
  public Optional<Payment> findById(String id) { return Optional.ofNullable(payments.get(id)); }

  @Override
  public List<Payment> findByUserId(String userId) {
    return payments.values().stream().filter(p -> p.getUserId().equals(userId)).toList();
  }
}