package com.garageid.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
public class PaymentService {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentGateway paymentGateway;

    public PaymentService(PaymentTransactionRepository paymentTransactionRepository, PaymentGateway paymentGateway) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentGateway = paymentGateway;
    }

    public PaymentTransaction process(String reservationId, BigDecimal amount, PaymentMethod method, String emailOrPhone) {
        PaymentTransaction tx = new PaymentTransaction();
        tx.setReservationId(reservationId);
        tx.setAmount(amount);
        tx.setMethod(method);
        tx.setCreatedAt(Instant.now());
        switch (method) {
            case PSE -> {
                String ext = paymentGateway.chargePse(amount, reservationId, emailOrPhone);
                tx.setExternalId(ext);
            }
            case NEQUI -> {
                String ext = paymentGateway.chargeNequi(amount, reservationId, emailOrPhone);
                tx.setExternalId(ext);
            }
            case CASH -> tx.setExternalId("CASH");
        }
        paymentTransactionRepository.save(tx);
        return tx;
    }
}