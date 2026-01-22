package com.BookingService.BookingService.dto.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedCostResponse {
    // Base trip cost before commission (T)
    private double baseTripCost;

    // Total amount tourist has to pay (T + 10%)
    private double touristPayAmount;

    // Amount driver / vehicle owner receives (T - 10%)
    private double driverReceiveAmount;

    // TripGeix platform earning (20% of T)
    private double platformEarning;

    private int estimatedDays;
}
