package com.tripgenix.AuthService.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name="`paid_amount`")
    private BigDecimal amount;

    @Column(name="`payment_date_time`")
    private LocalDateTime paymentDate;

    @Column(name="`payment_type`")
    private String paymentMethod;

//    @ManyToOne
//    @JoinColumn(name = "tourist_id", nullable = false)
//    private Tourist tourist;
}
