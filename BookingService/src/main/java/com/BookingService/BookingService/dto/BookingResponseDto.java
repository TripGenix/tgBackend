package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponseDto {
    private Long bookingId;
    private Long tripId;
    private String customerName;
    private List<String> route;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long touristId;
    private String status;
    private LocalDateTime createdAt;
    private String referenceId;
}
