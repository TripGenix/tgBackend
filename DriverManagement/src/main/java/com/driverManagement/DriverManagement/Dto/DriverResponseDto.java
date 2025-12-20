package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponseDto {
    private int driverId;

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String email;

    private String phone1;

    private String phone2;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String stateProvince;

    private String postalCode;

    private String licensePdfUrl;

    private int isApproved;

    private String status;

    private String driverImage;

    private LocalDateTime createdAt;

    private Boolean isDelete;

    private String nicNumber;

    private List<Integer> selectedVehicleCategories;

    private List<Integer> selectedVehicleByNumber;
}
