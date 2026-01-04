package com.googleMapService.googleMapServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponse {
    private double totalDistanceKm;
    private long totalDurationSeconds;
    private String totalDurationText;
    private String rawDirections;
}
