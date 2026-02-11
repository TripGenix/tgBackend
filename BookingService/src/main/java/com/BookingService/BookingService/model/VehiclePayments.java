package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "vehicle_payments",
        uniqueConstraints = @UniqueConstraint(columnNames = "booking_id")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePayments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String status;   // PAID, CANCELLED, PENDING, CONFIRM

    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;
}
