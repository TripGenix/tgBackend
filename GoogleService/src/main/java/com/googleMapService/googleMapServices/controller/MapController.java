package com.googleMapService.googleMapServices.controller;

import com.googleMapService.googleMapServices.Model.RouteRequest;
import com.googleMapService.googleMapServices.service.ShortestPath;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin("*")
public class MapController {

    private final ShortestPath shortestPath;

    public MapController(ShortestPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    @PostMapping("/shortest-route")
    public ResponseEntity<String> getShortestRoute(@RequestBody RouteRequest request) {
        return ResponseEntity.ok(shortestPath.getDirections(request));
    }
}
