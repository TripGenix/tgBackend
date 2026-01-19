package com.BookingService.BookingService.dto.BusinessModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedCostRequestDto {
    private Double distance;
    private Long vehicleId;
    private Long driverId;
    private Long tourGuidId;

}
