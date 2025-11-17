package com.garageid.payment;

import java.math.BigDecimal;

public interface PaymentGateway {
    String chargePse(BigDecimal amount, String reservationId, String userEmail);
    String chargeNequi(BigDecimal amount, String reservationId, String userPhone);
}