package com.googleMapService.googleMapServices.controller;

import com.googleMapService.googleMapServices.Model.RouteRequest;
import com.googleMapService.googleMapServices.service.PathwithDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/testapi")
public class testController {

    @Autowired
    private PathwithDistance pathwithDistance;

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("/shortest-route")
    public ResponseEntity<?> getShortestRoute(@RequestBody RouteRequest request) {
        return ResponseEntity.ok(pathwithDistance.getDirections(request));
    }
}
