package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverUpdateDto {

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

    private String licenseImageUrl;

    private int isApproved;

    private String status;

    private String driverImage;

    private String nicNumber;

    private List<Integer> selectedVehicleCategories;

    private List<Integer> selectedVehicleByNumber;
}
