package com.BookingService.BookingService.dto.systemReponse;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {
    private String transactionId;   // Formatted as TXN001
    private LocalDateTime date;     // From Payment.java model
    private String description;     // From Trip.java (Tour Name)
    private String method;          // From Payment.java (paymentType)
    private BigDecimal amount;      // From Payment.java (paidAmount)
    private String status;          // From Payment.java (PAID/PENDING)
}