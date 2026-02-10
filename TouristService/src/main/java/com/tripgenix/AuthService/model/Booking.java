package com.tripgenix.AuthService.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String destination;
    private LocalDate bookingDate;
    private String status;

    @ManyToOne
    @JoinColumn(name = "tourist_id")
    private Tourist tourist;
}
