package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDetailsDto {
    private String nameOfBooker;
    private String passportNumber;
    private String bookerEmail;
    private String bookerPhone;

    private LocalDateTime arrivalDateTime;
    private LocalDateTime departureDateTime;

    private String flightNumber;
    private String departureAirport;

    private PassengerDto passengers;
}
