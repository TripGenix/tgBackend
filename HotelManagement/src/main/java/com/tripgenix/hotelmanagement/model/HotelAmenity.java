package com.tripgenix.hotelmanagement.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class HotelAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Integer iconCode;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;
}