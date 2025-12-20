package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSaveDto {
    private String vehicleName;
    private String vehicleNumber;   // renamed from numberPlate if needed
    private Integer category;
    private Integer passengerCount;
    private BigDecimal costPerKm;
    private BigDecimal bookingPrice;
    private String status;
    private String description;

    // Owner Details
    private String ownerName;
    private String ownerId;
    private String phone;
    private String address1;
    private String address2;
    private String state;
    private String postalCode;
    private LocalDate dob;

    // Uploaded File URLs From Frontend
    private List<String> vehicleImages;  // multiple URLs
    private String ownerImage;           // single image URL
    private String documentUrl;

}
