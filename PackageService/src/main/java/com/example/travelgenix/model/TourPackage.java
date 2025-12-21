package com.example.travelgenix.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TourPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int passengers;
    private double price;
    private double totalPrice;
    private String duration;
    private String vehicle;
    private String guide;
    private boolean popular;

    @ElementCollection
    private List<String> destinations;

    @ElementCollection
    private List<String> features;

    @ElementCollection
    private List<String> hotels;
}
