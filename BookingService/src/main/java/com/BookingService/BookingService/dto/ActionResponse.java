package com.BookingService.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionResponse {
    private boolean success;
    private String message;

    public ActionResponse(boolean b, String tripConfirmedSuccessfully, Long bookingId) {
    }
}
