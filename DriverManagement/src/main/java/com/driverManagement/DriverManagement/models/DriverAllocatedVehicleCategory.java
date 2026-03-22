package com.driverManagement.DriverManagement.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "driver_allocated_vehicle_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverAllocatedVehicleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id", nullable = false)
    private Integer driverId;

    @Column(name = "category_id", nullable = false)
    private Integer categoryId;
}