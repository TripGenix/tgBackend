package com.example.travelgenix.payload;

import lombok.Data;

@Data
public class AdminStatusRequest {
    private String email;
    private boolean active;
}
