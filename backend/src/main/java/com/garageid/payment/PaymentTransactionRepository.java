package com.garageid.payment;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentTransactionRepository extends MongoRepository<PaymentTransaction, String> {
}