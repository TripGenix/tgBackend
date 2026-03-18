package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverStatusResponse {
    private Long bookingId;
    private String status;
    private String message;
    private String driverName;
    private String driverPhone;
    private String vehicleNumber;
    private String otp;
}
