package com.vehicelManagement.vehicelManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="vehicle_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehicleCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "category")
    private String category;
}
