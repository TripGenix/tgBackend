package com.tripgenix.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class HotelReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String reviewerName;
    private Double rate;
    private String comment;
    private String reviewerImage;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}