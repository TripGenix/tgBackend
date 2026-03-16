package com.googleMapService.googleMapServices.controller;

import com.googleMapService.googleMapServices.DTO.PlaceDto;
import com.googleMapService.googleMapServices.service.PlacesService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/places")
@CrossOrigin("*")
public class PlacesController {

    private final PlacesService placesService;

    public PlacesController(PlacesService placesService) {
        this.placesService = placesService;
    }

    @GetMapping("/{district}")
    public List<PlaceDto> getBestPlacesByDistrict(@PathVariable String district) {
        return placesService.getBestPlaces(district);
    }
}
