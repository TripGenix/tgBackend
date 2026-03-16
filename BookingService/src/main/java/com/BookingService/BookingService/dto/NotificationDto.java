package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String type; // "NEW_BOOKING", "BOOKING_UPDATE", etc.
    private String title;
    private String message;
    private Long bookingId;
    private Integer driverId;
    private Map<String, Object> data; // Additional payload
}

