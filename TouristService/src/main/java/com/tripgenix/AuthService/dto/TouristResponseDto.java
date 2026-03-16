package com.tripgenix.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class TouristResponseDto {
    private Integer touristId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String country;
    private String passportNicNumber;
    private String profileImageUrl;
    private LocalDateTime createdAt;
}
