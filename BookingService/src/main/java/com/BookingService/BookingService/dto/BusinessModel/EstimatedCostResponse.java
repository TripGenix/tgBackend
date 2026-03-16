package com.BookingService.BookingService.dto.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedCostResponse {
    private BigDecimal baseCost;
    private BigDecimal touristPays;

    private BigDecimal driverReceives;
    private BigDecimal vehicleReceives;
    private BigDecimal platformCommission;

    private int tripDays;

}
