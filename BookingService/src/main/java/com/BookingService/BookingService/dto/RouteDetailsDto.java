package com.BookingService.BookingService.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDetailsDto {
    private Double distance;
    private Double duration;
    private String polyline;

    private Double costPerKm;
    private Double bookingPrice;
}
