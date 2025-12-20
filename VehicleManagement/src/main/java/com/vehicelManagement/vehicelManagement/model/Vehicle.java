package com.vehicelManagement.vehicelManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="vehicle")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private int vehicleId;


    @Column(name = "number_plate")
    private String numberPlate;
    private Integer type;
    private String description;
    @Column(name = "passenger_count")
    private Integer passengerCount;

    @Column(name = "cost_per_km")
    private BigDecimal costPerKm;
    @Column(name = "booking_price")
    private BigDecimal bookingPrice;

    private String status;

    @CreationTimestamp
    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_delete")
    private Boolean isDelete = false;

    @Column(name="vehicle_name")
    private String vehicleName;

    @Column(name="document_url")
    private String documentUrl;

    // FK to owner table
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

}
