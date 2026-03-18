package com.tripgenix.AuthService.dto;

import lombok.Data;

@Data
public class LoginResposnseDto {
    private String token;
    private long userId;

    public LoginResposnseDto(String token, long userId) {
        this.token = token;
        this.userId=userId;

    }
}
