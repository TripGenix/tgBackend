package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRegisterResponseDto {
    private String message;
    private String email;
    private Boolean emailVerified;
}

