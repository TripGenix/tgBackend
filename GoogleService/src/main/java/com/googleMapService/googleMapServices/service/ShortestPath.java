package com.googleMapService.googleMapServices.service;

import com.googleMapService.googleMapServices.Model.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class ShortestPath {

    @Value("${google.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getDirections(RouteRequest request) {
        try {
            // 1. ENCODE PARAMETERS (Fixes "Nuwara Eliya" -> "Nuwara%20Eliya")
            String origin = URLEncoder.encode(request.getStart() + ", Sri Lanka", StandardCharsets.UTF_8);
            String destination = URLEncoder.encode(request.getEnd() + ", Sri Lanka", StandardCharsets.UTF_8);

            StringBuilder waypointsParam = new StringBuilder();

            if (request.getWaypoints() != null && !request.getWaypoints().isEmpty()) {
                if ("walking".equals(request.getMode())) {
                    waypointsParam.append("&waypoints=");
                } else {
                    waypointsParam.append("&waypoints=optimize:true");
                }

                for (String wp : request.getWaypoints()) {
                    // Clean brackets just in case
                    String cleanWp = wp.replace("[", "").replace("]", "").trim();

                    if (!cleanWp.isEmpty()) {
                        // 2. ENCODE PIPE AND CITY NAME
                        // This ensures "Nuwara Eliya" becomes safe for the URL
                        waypointsParam.append("|").append(URLEncoder.encode(cleanWp + ", Sri Lanka", StandardCharsets.UTF_8));
                    }
                }
            }

            // 3. CONSTRUCT URL
            String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "origin=" + origin +
                    "&destination=" + destination +
                    waypointsParam.toString() +
                    "&mode=" + (request.getMode() != null ? request.getMode() : "driving") +
                    "&key=" + apiKey;

            System.out.println("🚀 Backend Calling Google: " + url);

            return restTemplate.getForObject(url, String.class);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}