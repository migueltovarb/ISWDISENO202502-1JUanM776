package com.garageid.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MockPaymentGateway implements PaymentGateway {
    @Override
    public String chargePse(BigDecimal amount, String reservationId, String userEmail) {
        return "PSE-" + UUID.randomUUID();
    }

    @Override
    public String chargeNequi(BigDecimal amount, String reservationId, String userPhone) {
        return "NEQUI-" + UUID.randomUUID();
    }
}