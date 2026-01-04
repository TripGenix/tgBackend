package com.tripgenix.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name="hotel")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double latitude;
    private Double longitude;
    private String name;
    private String location;

    @Column(length = 2000)
    private String description;

    private String image; // Main thumbnail
    private Double pricePerNight;
    private Double rating;
    private Integer reviewsCount;
    private Boolean isFavorite;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelAmenity> amenities;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    private List<HotelReview> reviews;

    @ElementCollection
    @CollectionTable(name = "hotel_gallery", joinColumns = @JoinColumn(name = "hotel_id"))
    @Column(name = "image_url")
    private List<String> gallery;
}