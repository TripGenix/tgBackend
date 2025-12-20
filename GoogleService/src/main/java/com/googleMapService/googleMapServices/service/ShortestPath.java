package com.googleMapService.googleMapServices.service;

import com.googleMapService.googleMapServices.Model.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShortestPath {

    @Value("${google.api.key}")
    private String apiKey;

    public String getDirections(RouteRequest routeRequest) {
        RestTemplate restTemplate = new RestTemplate();

        String waypointsParam = "";
        if (routeRequest.getWaypoints() != null && !routeRequest.getWaypoints().isEmpty()) {
            waypointsParam = "&waypoints=optimize:true|" +
                    String.join("|", routeRequest.getWaypoints());
        }

        String url =
                "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=" + routeRequest.getStart() +
                        "&destination=" + routeRequest.getEnd() +
                        waypointsParam +
                        "&mode=driving" +
                        "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}
