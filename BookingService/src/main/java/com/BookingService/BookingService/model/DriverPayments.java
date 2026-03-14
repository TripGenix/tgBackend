package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "driver_payments",
        uniqueConstraints = @UniqueConstraint(columnNames = "booking_id"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverPayments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;   // PAID, CANCELLED, PENDING,CONFIRM

    private LocalDateTime paymentDateTime;

    @ManyToOne
    private Driver driver;

}
