package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebVehicleResponse {
    private Integer vehicleId;
    private String numberPlate;
    private String type;
    private String description;
    private Integer passengerCount;
    private BigDecimal costPerKm;
    private BigDecimal bookingPrice;
    private String status;
    private String vehicleName;
    private Double latitude;
    private Double longitude;
    private List<String> vehicleImages;
    private String location;
    private Integer doorCount;
    private Double power;
    private Double topSpeed;
    private Double acceleration;
    private String mainImage;

}
