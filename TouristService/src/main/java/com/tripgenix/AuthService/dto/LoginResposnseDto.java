package com.tripgenix.AuthService.dto;

import lombok.Data;

@Data
public class LoginResposnseDto {
    private String token;

    public LoginResposnseDto(String token) {
        this.token = token;
    }
}
