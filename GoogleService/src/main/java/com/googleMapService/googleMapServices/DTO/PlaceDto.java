package com.googleMapService.googleMapServices.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceDto {
    private String name;
    private double rating;
    private int userRatingCount;
    private String address;
    private String photoUrl;
    private String mapImageUrl;
    private double score;
}
