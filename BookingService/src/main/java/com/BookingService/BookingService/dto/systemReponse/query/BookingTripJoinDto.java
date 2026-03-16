package com.BookingService.BookingService.dto.systemReponse.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingTripJoinDto {

    // ===== BOOKING TABLE =====
    private Long bookingId;
    private String referenceId;
    private Integer touristId;
    private Long tripId;
    private Integer packageId;
    private Integer vehicleId;
    private Integer driverId;

    private String bookerName;
    private String bookerEmail;
    private String bookerPhone;
    private String passportNumber;

    private Integer adults;
    private Integer children;
    private Integer babies;

    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;

    private String flightNumber;
    private String departureAirport;

    private String bookingStatus;

    private Boolean isTouristConfirm;
    private LocalDateTime touristConfirmedAt;

    private Boolean isTouristCancelled;
    private LocalDateTime touristCancelledAt;

    private Boolean isDriverConfirm;
    private LocalDateTime driverConfirmedAt;

    private Boolean isDriverCancelled;
    private LocalDateTime driverCancelledAt;

    private Boolean isSendConfirmEmail;
    private LocalDateTime bookingCreatedAt;

    // ===== TRIP TABLE =====
    private LocalDateTime tripStartDateTime;
    private LocalDateTime tripEndDateTime;

    private BigDecimal estimatedCost;
    private String tripStatus;

    private Double duration;
    private Double distance;

    private String startLocation;
    private String endLocation;

}
