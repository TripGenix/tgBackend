package com.BookingService.BookingService.dto;

import com.EmailService.EmailService.Dto.EmailDetailsDto;
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
