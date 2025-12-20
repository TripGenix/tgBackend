package com.googleMapService.googleMapServices.Model;

import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {
    private String start;
    private String end;
    private List<String> waypoints;
}
