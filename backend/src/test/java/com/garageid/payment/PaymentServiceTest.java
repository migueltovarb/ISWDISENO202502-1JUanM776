package com.garageid.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {
    private PaymentTransactionRepository paymentTransactionRepository;
    private PaymentGateway paymentGateway;
    private PaymentService paymentService;

    @BeforeEach
    @SuppressWarnings("unused")
    void setup() {
        paymentTransactionRepository = Mockito.mock(PaymentTransactionRepository.class);
        paymentGateway = Mockito.mock(PaymentGateway.class);
        paymentService = new PaymentService(paymentTransactionRepository, paymentGateway);
    }

    @Test
    void testCashSetsExternalId() {
        PaymentTransaction tx = paymentService.process("R1", new BigDecimal("10.00"), PaymentMethod.CASH, "user@example.com");
        assertEquals("CASH", tx.getExternalId());
        verify(paymentTransactionRepository, times(1)).save(any());
    }

    @Test
    void testPseUsesGateway() {
        when(paymentGateway.chargePse(new BigDecimal("20.00"), "R2", "user@example.com"))
                .thenReturn("PSE-123");
        PaymentTransaction tx = paymentService.process("R2", new BigDecimal("20.00"), PaymentMethod.PSE, "user@example.com");
        assertEquals("PSE-123", tx.getExternalId());
        verify(paymentTransactionRepository, times(1)).save(any());
    }

    @Test
    void testNequiUsesGateway() {
        when(paymentGateway.chargeNequi(new BigDecimal("30.00"), "R3", "3000000000"))
                .thenReturn("NEQUI-123");
        PaymentTransaction tx = paymentService.process("R3", new BigDecimal("30.00"), PaymentMethod.NEQUI, "3000000000");
        assertEquals("NEQUI-123", tx.getExternalId());
        verify(paymentTransactionRepository, times(1)).save(any());
    }
}