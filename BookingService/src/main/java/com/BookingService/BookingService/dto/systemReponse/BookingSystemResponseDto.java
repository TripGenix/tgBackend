package com.BookingService.BookingService.dto.systemReponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSystemResponseDto {

    // 🔹 IDs
    private Long bookingId;
    private String referenceId;
    private Long touristId;
    private Long tripId;
    private Long packageId;
    private Long vehicleId;
    private Long driverId;

    // 🔹 Booker details
    private String bookerName;
    private String bookerEmail;
    private String bookerPhone;
    private String passportNumber;

    // 🔹 Passenger counts
    private Integer adults;
    private Integer children;
    private Integer babies;

    // 🔹 Travel details
    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;
    private String flightNumber;
    private String departureAirport;

    // 🔹 Trip route (derived / joined)
    private List<String> route;
    private Boolean isTourStart;
    private Boolean isTourEnd;

    // 🔹 Tour dates
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    // 🔹 Status
    private String status;

    // 🔹 Tourist confirmation
    private Boolean isTouristConfirm;
    private LocalDateTime touristConfirmedAt;

    private Boolean isTouristCancelled;
    private LocalDateTime touristCancelledAt;

    // 🔹 Driver confirmation
    private Boolean isDriverConfirm;
    private LocalDateTime driverConfirmedAt;

    private Boolean isDriverCancelled;
    private LocalDateTime driverCancelledAt;

    // 🔹 Email status
    private Boolean sendConfirmEmail;

    // 🔹 System timestamps
    private LocalDateTime createdAt;
}
