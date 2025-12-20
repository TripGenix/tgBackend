package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Integer vehicleId;
    private String numberPlate;
    private String type;
    private String description;
    private Integer passengerCount;
    private BigDecimal costPerKm;
    private BigDecimal bookingPrice;
    private String status;
    private LocalDateTime createdAt;
    private String vehicleName;
}
