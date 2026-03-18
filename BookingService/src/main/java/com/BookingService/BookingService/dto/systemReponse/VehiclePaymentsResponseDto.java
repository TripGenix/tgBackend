package com.BookingService.BookingService.dto.systemReponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclePaymentsResponseDto {
    private Long id;


    private Long paymentId;

    private Long bookingId;
    private String refereeId;
    private  String vehicleNumber;

    private BigDecimal amount;

    private String status;   // PAID, CANCELLED, PENDING,CONFIRM
    private LocalDateTime paymentDateTime;
}
