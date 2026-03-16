package com.tripgenix.hotelmanagement.dto;

import lombok.Data;
import java.util.List;

@Data
public class HotelDto {
    private String id;
    private String name;
    private String location;
    private String image;
    private Double latitude;
    private Double longitude;
    private double pricePerNight;
    private double rating;
    private int reviewsCount;
    private String description;
    private boolean isFavorite;

    private List<String> gallery;
    private List<AmenityDto> amenities;
    private List<ReviewDto> reviews;
}