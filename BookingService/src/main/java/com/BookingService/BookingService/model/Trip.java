package com.BookingService.BookingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trip_id")
    private Long tripId;

    @Column(name = "`start_dateTime`")
    private LocalDateTime startDateTime;

    @Column(name = "`end_dateTime`")
    private LocalDateTime endDateTime;

    @Column(name = "`estimated_cost`")
    private BigDecimal estimatedCost;

    private String status;

    private Double duration;
    private Double distance;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "end_location")
    private String endLocation;
}
