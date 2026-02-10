package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments",
        uniqueConstraints = @UniqueConstraint(columnNames = "booking_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private BigDecimal paidAmount;

    @Column(name="balance")
    private BigDecimal balance;

    @Column(nullable = false)
    private String status;   // PAID, ADVANCED, CANCELLED, PENDING

    private String paymentType; // CARD, PAYHERE, CASH

    private LocalDateTime paymentDateTime;
}
