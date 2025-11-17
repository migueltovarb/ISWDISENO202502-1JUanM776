package com.garageid.admin;

import com.garageid.payment.PaymentMethod;
import com.garageid.payment.PaymentTransaction;
import com.garageid.payment.PaymentTransactionRepository;
import com.garageid.reservation.Reservation;
import com.garageid.reservation.ReservationRepository;
import com.garageid.reservation.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminControllerTest {
    private ReservationRepository reservationRepository;
    private PaymentTransactionRepository paymentTransactionRepository;
    private AdminController adminController;

    @BeforeEach
    void setup() {
        reservationRepository = mock(ReservationRepository.class);
        paymentTransactionRepository = mock(PaymentTransactionRepository.class);
        adminController = new AdminController(reservationRepository, paymentTransactionRepository);
    }

    @Test
    void testOccupancyCountsOverlap() {
        when(reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(eq("P1"), anyList(), any(), any()))
                .thenReturn(List.of(new Reservation(), new Reservation()));
        var res = adminController.occupancy("P1", Instant.now(), Instant.now().plusSeconds(3600));
        assertEquals(200, res.getStatusCode().value());
        assertEquals(2, res.getBody());
    }

    @Test
    void testIncomeSumsPaymentsInRange() {
        PaymentTransaction t1 = new PaymentTransaction();
        t1.setAmount(new BigDecimal("10.00"));
        t1.setCreatedAt(Instant.now());
        t1.setMethod(PaymentMethod.CASH);
        PaymentTransaction t2 = new PaymentTransaction();
        t2.setAmount(new BigDecimal("5.00"));
        t2.setCreatedAt(Instant.now());
        when(paymentTransactionRepository.findAll()).thenReturn(List.of(t1, t2));
        var res = adminController.income(Instant.now().minusSeconds(10), Instant.now().plusSeconds(10));
        assertEquals(new BigDecimal("15.00"), res.getBody());
    }

    @Test
    void testExportCsvFormat() {
        PaymentTransaction t = new PaymentTransaction();
        t.setReservationId("R1");
        t.setAmount(new BigDecimal("7.00"));
        t.setMethod(PaymentMethod.PSE);
        when(paymentTransactionRepository.findAll()).thenReturn(List.of(t));
        var res = adminController.export();
        assertEquals(200, res.getStatusCode().value());
        String csv = new String(res.getBody(), StandardCharsets.UTF_8);
        assertTrue(csv.contains("reservationId,amount,method"));
        assertTrue(csv.contains("R1,7.00,PSE"));
    }
}