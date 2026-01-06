package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleReciveDto {
    private Integer vehicleId;
    private String numberPlate;
    private Integer type;
    private String description;
    private Integer passengerCount;
    private BigDecimal costPerKm;
    private Double latitude;
    private Double longitude;
    private BigDecimal bookingPrice;
    private String status;
    private LocalDateTime createdAt;
    private String vehicleName;
    private List<String> vehicleImages;
    private String documentUrl;
    private OwnerDto owner;

    private String location;

}
