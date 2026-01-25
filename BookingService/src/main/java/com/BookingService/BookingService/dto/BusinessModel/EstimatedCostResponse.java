package com.BookingService.BookingService.dto.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedCostResponse {
    private Double baseTripCost;
    private Double touristPayAmount;
    private Double driverReceives;
    private Double tripGeixEarns;
    private int estimatedDays;

}
