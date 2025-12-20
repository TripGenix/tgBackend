package com.googleMapService.googleMapServices.service;

import com.googleMapService.googleMapServices.DTO.PlaceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class PlacesService {

    @Value("${google.api.key}")
    private String googleApiKey;

    private final String PLACES_API_URL =
            "https://places.googleapis.com/v1/places:searchText";

    private final List<String> ALLOWED_TYPES = List.of(
            "tourist_attraction", "museum", "park", "hindu_temple",
            "buddhist_temple", "church", "mosque", "zoo",
            "view_point", "aquarium", "beach"
    );

    public List<PlaceDto> getBestPlaces(String district) {

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestBody = Map.of(
                "textQuery", "tourist attractions in " + district + " Sri Lanka",
                "maxResultCount", 20,
                "languageCode", "en"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Goog-Api-Key", googleApiKey);
        headers.set("X-Goog-FieldMask",
                "places.displayName,places.formattedAddress," +
                        "places.rating,places.userRatingCount,places.photos," +
                        "places.location,places.types");

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response =
                restTemplate.exchange(PLACES_API_URL, HttpMethod.POST, entity, Map.class);

        List<Map<String, Object>> places =
                (List<Map<String, Object>>) response.getBody().get("places");

        List<PlaceDto> results = new ArrayList<>();

        if (places == null) return results;

        for (Map<String, Object> p : places) {

            // rating safe cast
            Object ratingObj = p.getOrDefault("rating", 0);
            double rating = ((Number) ratingObj).doubleValue();

            // review count safe cast
            Object reviewObj = p.getOrDefault("userRatingCount", 0);
            int reviews = ((Number) reviewObj).intValue();

            if (rating < 4.0 || reviews < 50) continue;

            List<String> types = (List<String>) p.get("types");

            if (types == null || Collections.disjoint(types, ALLOWED_TYPES))
                continue;

            Map<String, Object> location =
                    (Map<String, Object>) p.get("location");

            Number latN = (Number) location.get("latitude");
            Number lngN = (Number) location.get("longitude");
            double lat = latN.doubleValue();
            double lng = lngN.doubleValue();

            List<Map<String, Object>> photos =
                    (List<Map<String, Object>>) p.get("photos");

            String photoUrl = null;
            if (photos != null && !photos.isEmpty()) {
                String name = (String) photos.get(0).get("name");
                photoUrl = "https://places.googleapis.com/v1/" + name +
                        "/media?maxHeightPx=400&key=" + googleApiKey;
            }

            double score = rating * reviews;

            PlaceDto dto = new PlaceDto(
                    (String) ((Map) p.get("displayName")).get("text"),
                    rating,
                    reviews,
                    (String) p.get("formattedAddress"),
                    photoUrl,
                    "https://maps.googleapis.com/maps/api/staticmap?center=" +
                            lat + "," + lng + "&zoom=14&size=400x200&markers=color:red|" +
                            lat + "," + lng + "&key=" + googleApiKey,
                    score
            );

            results.add(dto);
        }

        // sort by score
        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results;
    }
}
