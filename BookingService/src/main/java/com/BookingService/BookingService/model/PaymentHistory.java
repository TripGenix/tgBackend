package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(nullable = false)
    private String paymentType; // CARD, CASH, PAYHERE

    @Column(nullable = false)
    private LocalDateTime paidDateTime;

    @Column(nullable = false)
    private String status; // SUCCESS, FAILED
}
