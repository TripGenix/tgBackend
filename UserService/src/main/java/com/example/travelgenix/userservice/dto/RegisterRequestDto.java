package com.example.travelgenix.userservice.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDto {
    private String firstName;
    private String lastName;
    private String nic;
    private LocalDate dob;
    private String email;
    private String password;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
}
