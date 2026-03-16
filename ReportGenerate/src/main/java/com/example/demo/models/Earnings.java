package com.example.demo.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "company_earn")
public class Earnings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Column(name = "booking_id")
    private Long bookingId;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_date_time")
    private LocalDateTime paymentDateTime;

    public Earnings() {}

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public LocalDateTime getPaymentDateTime() {
        return paymentDateTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentDateTime(LocalDateTime paymentDateTime) {
        this.paymentDateTime = paymentDateTime;
    }
}