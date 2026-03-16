package com.example.travelgenix.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
@Table(name = "travel_packages")
public class TravelPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String description;
    private String coverImage;

    @Enumerated(EnumType.STRING)
    private PackageCategory category;

    // --- EXTERNAL IDs (Linking to other services) ---
    private Integer hotelId;   // Store only the ID
    private Integer vehicleId; // Store only the ID

    private Integer durationDays;
    private Double basePrice;
    private Double discountPercentage; // e.g., 0.15

    @OneToMany(mappedBy = "travelPackage", cascade = CascadeType.ALL)
    private List<PackageActivity> activities;
}