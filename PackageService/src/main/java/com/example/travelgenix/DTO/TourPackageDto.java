package com.example.travelgenix.DTO;

import lombok.Data;
import java.util.List;

@Data
public class TourPackageDto {
    private Long id;
    private String name;
    private int passengers;
    private double price;
    private double totalPrice;
    private String duration;
    private String vehicle;
    private String guide;
    private boolean popular;
    private List<String> destinations;
    private List<String> features;
    private List<String> hotels;
}
