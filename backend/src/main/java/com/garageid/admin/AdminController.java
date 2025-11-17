package com.garageid.admin;
import com.garageid.payment.PaymentTransaction;
import com.garageid.payment.PaymentTransactionRepository;
import com.garageid.reservation.Reservation;
import com.garageid.reservation.ReservationRepository;
import com.garageid.reservation.ReservationStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final ReservationRepository reservationRepository;
    private final PaymentTransactionRepository paymentTransactionRepository;

    public AdminController(ReservationRepository reservationRepository, PaymentTransactionRepository paymentTransactionRepository) {
        this.reservationRepository = reservationRepository;
        this.paymentTransactionRepository = paymentTransactionRepository;
    }

    @GetMapping("/reports/occupancy")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> occupancy(@RequestParam String parkingId, @RequestParam Instant start, @RequestParam Instant end) {
        List<Reservation> res = reservationRepository.findByParkingIdAndStatusInAndStartTimeLessThanAndEndTimeGreaterThan(parkingId, List.of(ReservationStatus.CONFIRMED, ReservationStatus.IN_USE, ReservationStatus.FINISHED), end, start);
        int count = res.size();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/reports/income")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> income(@RequestParam Instant start, @RequestParam Instant end) {
        List<PaymentTransaction> txs = paymentTransactionRepository.findAll().stream().filter(t -> t.getCreatedAt() != null && !t.getCreatedAt().isBefore(start) && !t.getCreatedAt().isAfter(end)).collect(Collectors.toList());
        BigDecimal total = txs.stream().map(PaymentTransaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        return ResponseEntity.ok(total);
    }

    @GetMapping("/reports/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> export() {
        StringBuilder sb = new StringBuilder();
        sb.append("reservationId,amount,method\n");
        for (PaymentTransaction t : paymentTransactionRepository.findAll()) {
            sb.append(t.getReservationId()).append(",").append(t.getAmount()).append(",").append(t.getMethod()).append("\n");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=report.csv").contentType(MediaType.TEXT_PLAIN).body(bytes);
    }
}