package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverLoginResponseDto {
    private String token;
    private int driverId;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean emailVerified;
    private Boolean isApproved;
}

