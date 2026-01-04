package com.example.travelgenix.DTO;

import com.example.travelgenix.model.PackageCategory;
import lombok.Data;
import java.util.List;

@Data
public class PackageDto {
    private String id;
    private String name;
    private String description;
    private String coverImage;
    private PackageCategory category;

    private Integer hotelId;
    private Integer vehicleId;

    private int durationDays;
    private double basePrice;
    private double discountPercentage;

    private List<ActivityDto> activities;

    // Calculated fields (optional, can be done in frontend too)
    public double getTotalPrice() {
        return basePrice * (1 - discountPercentage);
    }

    public double getSavingsAmount() {
        return basePrice * discountPercentage;
    }
}