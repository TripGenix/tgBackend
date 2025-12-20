package com.vehicelManagement.vehicelManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUpdateDto {
    private String vehicleName;
    private String vehicleNumber;
    private Integer category;
    private String description;
    private int passengerCount;
    private double costPerKm;
    private double bookingPrice;
    private String status;

    private String ownerName;
    private String ownerId;
    private String phone;
    private String address1;
    private String address2;
    private String state;
    private String postalCode;
    private String dob;

    private List<String> vehicleImages;
    private String ownerImage;
    private String documentUrl;
}

