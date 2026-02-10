package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRegisterDto {
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
    private String nicNumber;
    private String password;
}

