package com.garageid.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentTransactionRepository paymentTransactionRepository;
    private final PaymentService paymentService;

    public PaymentController(PaymentTransactionRepository paymentTransactionRepository, PaymentService paymentService) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentService = paymentService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<PaymentTransaction> list() { return paymentTransactionRepository.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR')")
    public ResponseEntity<?> create(Authentication auth,
                                    @RequestParam @NotBlank String reservationId,
                                    @RequestParam @NotNull BigDecimal amount,
                                    @RequestParam @NotNull PaymentMethod method,
                                    @RequestParam(required = false) String contact) {
        PaymentTransaction tx = paymentService.process(reservationId, amount, method, contact != null ? contact : auth.getName());
        return ResponseEntity.ok(tx);
    }

    @GetMapping("/{id}/receipt")
    @PreAuthorize("hasRole('USER') or hasRole('OPERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> receipt(@PathVariable String id) {
        PaymentTransaction tx = paymentTransactionRepository.findById(id).orElse(null);
        if (tx == null) return ResponseEntity.notFound().build();
        Receipt r = new Receipt(tx.getId(), tx.getReservationId(), tx.getAmount(), tx.getMethod(), tx.getExternalId());
        return ResponseEntity.ok(r);
    }

    public static class Receipt {
        private final String id;
        private final String reservationId;
        private final BigDecimal amount;
        private final PaymentMethod method;
        private final String externalId;

        public Receipt(String id, String reservationId, BigDecimal amount, PaymentMethod method, String externalId) {
            this.id = id; this.reservationId = reservationId; this.amount = amount; this.method = method; this.externalId = externalId;
        }
        public String getId() { return id; }
        public String getReservationId() { return reservationId; }
        public BigDecimal getAmount() { return amount; }
        public PaymentMethod getMethod() { return method; }
        public String getExternalId() { return externalId; }
    }
}