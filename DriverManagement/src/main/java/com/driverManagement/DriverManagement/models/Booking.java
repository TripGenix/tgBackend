package com.driverManagement.DriverManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookingId;

    private int driverId;

    @Column(name = "is_driver_confirm", nullable = false)
    private Boolean isDriverConfirm;

    @Column(name = "driver_confirmed_at")
    private LocalDateTime driverConfirmedAt;

    @Column(name = "is_driver_cancelled", nullable = false)
    private Boolean isDriverCancelled ;

    @Column(name = "driver_cancelled_at")
    private LocalDateTime driverCancelledAt;

    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "tourist_id", nullable = false)
    private Long touristId;


}
