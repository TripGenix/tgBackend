package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    // =========================
    // Primary Key
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long bookingId;

    // =========================
    // Reference
    // =========================
    @Column(name = "reference_id", nullable = false, unique = true)
    private String referenceId;

    // =========================
    // Relations (IDs only)
    // =========================
    @Column(name = "tourist_id", nullable = false)
    private Long touristId;

    @Column(name = "trip_id", nullable = false)
    private Long tripId;

    @Column(name = "package_id", nullable = false)
    private Integer packageId;

    @Column(name = "vehicle_id")
    private Integer vehicleId;

    @Column(name = "driver_id")
    private Integer driverId;

    // =========================
    // Booker Details
    // =========================
    @Column(name = "booker_name", nullable = false)
    private String bookerName;

    @Column(name = "booker_email", nullable = false)
    private String bookerEmail;

    @Column(name = "booker_phone", nullable = false)
    private String bookerPhone;

    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    // =========================
    // Passenger Counts
    // =========================
    @Column(name = "adults", nullable = false)
    private Integer adults;

    @Column(name = "children", nullable = false)
    private Integer children;

    @Column(name = "babies", nullable = false)
    private Integer babies;

    // =========================
    // Flight Details
    // =========================
    @Column(name = "arrival_date_time", nullable = false)
    private LocalDateTime arrivalDateTime;

    @Column(name = "departure_date_time", nullable = false)
    private LocalDateTime departureDateTime;

    @Column(name = "flight_number", nullable = false)
    private String flightNumber;

    @Column(name = "departure_airport", nullable = false)
    private String departureAirport;

    // =========================
    // Status
    // =========================
    @Column(name = "status", nullable = false)
    private String status;

    // =========================
    // Tourist Confirmation
    // =========================
    @Column(name = "is_tourist_confirm", nullable = false)
    private Boolean isTouristConfirm = false;

    @Column(name = "tourist_confirmed_at")
    private LocalDateTime touristConfirmedAt;

    @Column(name = "is_tourist_cancelled", nullable = false)
    private Boolean isTouristCancelled = false;

    @Column(name = "tourist_cancelled_at")
    private LocalDateTime touristCancelledAt;

    // =========================
    // Driver Confirmation
    // =========================
    @Column(name = "is_driver_confirm", nullable = false)
    private Boolean isDriverConfirm = false;

    @Column(name = "driver_confirmed_at")
    private LocalDateTime driverConfirmedAt;

    @Column(name = "is_driver_cancelled", nullable = false)
    private Boolean isDriverCancelled = false;

    @Column(name = "driver_cancelled_at")
    private LocalDateTime driverCancelledAt;

    // =========================
    // System
    // =========================
    @Column(name = "is_send_confirm_email", nullable = false)
    private Boolean sendConfirmEmail = false;

    @Column(name = "date_created", nullable = false)
    private LocalDateTime dateCreated;
}
