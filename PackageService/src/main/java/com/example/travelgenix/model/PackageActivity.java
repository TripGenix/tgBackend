package com.example.travelgenix.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "package_activities")
public class PackageActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;
    private String icon; // e.g., "sun"
    private String timeSlot; // e.g., "Morning"

    @ManyToOne
    @JoinColumn(name = "package_id")
    private TravelPackage travelPackage;
}