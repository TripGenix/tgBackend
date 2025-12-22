package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripDetailsDto {
    private String startLocation;
    private String endLocation;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean isVehicle;

    private List<String> destinations;
}
