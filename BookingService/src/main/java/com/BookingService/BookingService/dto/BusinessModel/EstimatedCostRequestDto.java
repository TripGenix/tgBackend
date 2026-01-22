package com.BookingService.BookingService.dto.BusinessModel;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstimatedCostRequestDto {

    @NotNull
    @Positive
    private Double distance;

    @NotNull
    @Positive
    private Integer totalDays=1;

    @NotNull
    private Long vehicleId;

    private Long driverId;
    private Long tourGuideId;

}
