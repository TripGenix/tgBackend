package com.googleMapService.googleMapServices.service;

import com.googleMapService.googleMapServices.DTO.RouteResponse;
import com.googleMapService.googleMapServices.Model.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class PathwithDistance {
    @Value("${google.api.key}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public RouteResponse getDirections(RouteRequest routeRequest) {

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

        String response = restTemplate.getForObject(url, String.class);

        return calculateTotals(response);
    }

    private RouteResponse calculateTotals(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode legs = root.path("routes").get(0).path("legs");

            long totalDistanceMeters = 0;
            long totalDurationSeconds = 0;

            for (JsonNode leg : legs) {
                totalDistanceMeters += leg.path("distance").path("value").asLong();
                totalDurationSeconds += leg.path("duration").path("value").asLong();
            }

            double totalDistanceKm = totalDistanceMeters / 1000.0;

            String durationText = formatDuration(totalDurationSeconds);

            return new RouteResponse(
                    totalDistanceKm,
                    totalDurationSeconds,
                    durationText,
                    json
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Google Directions response", e);
        }
    }

    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        if (hours > 0) {
            return hours + " hours " + minutes + " mins";
        }
        return minutes + " mins";
    }
}
