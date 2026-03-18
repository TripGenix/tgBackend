package com.example.travelgenix.payload;

import lombok.Data;

@Data
public class AdminUpdateRequest {
    private String oldEmail;
    private String username;
    private String newEmail;
}

