package com.driverManagement.DriverManagement.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_allocated_vehicle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverAllocatedVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", nullable = false)
    private Integer driverId;

    @Column(name = "vehicle_id", nullable = false)
    private Integer vehicleId;
}