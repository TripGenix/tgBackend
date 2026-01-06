package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourcesDto {
    private VehicleDto vehicle;
    private DriverDto driver;
    private GuideDto guide;
}
