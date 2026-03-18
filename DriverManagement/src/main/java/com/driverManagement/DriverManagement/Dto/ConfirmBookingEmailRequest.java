package com.driverManagement.DriverManagement.Dto;

import com.BookingService.BookingService.dto.EmailDetailsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmBookingEmailRequest {
    private Long bookingId;
    private EmailDetailsDto email;
}
