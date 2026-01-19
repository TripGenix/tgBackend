package com.googleMapService.googleMapServices.service;

import com.googleMapService.googleMapServices.Model.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ShortestPath {

    private final RouteRepository routeRepository;
    @Value("${google.api.key}")
    private String apiKey;

    public ShortestPath(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
    }

    public String getDirections(RouteRequest routeRequest) {
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(routeRequest);
        String waypointsParam = "";
        if (routeRequest.getWaypoints() != null && !routeRequest.getWaypoints().isEmpty()) {
            if ("walking".equals(routeRequest.getMode())) {
                waypointsParam = "&waypoints=" +
                        String.join("|", routeRequest.getWaypoints());
            } else {
                waypointsParam = "&waypoints=optimize:true|" +
                        String.join("|", routeRequest.getWaypoints());
            }
        }


        String url =
                "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=" + routeRequest.getStart() +
                        "&destination=" + routeRequest.getEnd() +
                        waypointsParam +
                        "&mode=" + routeRequest.getMode() +
                        "&key=" + apiKey;

        return restTemplate.getForObject(url, String.class);
    }
}
