package com.vehicelManagement.vehicelManagement.controller;

import com.vehicelManagement.vehicelManagement.dto.WebVehicleResponse;
import com.vehicelManagement.vehicelManagement.service.VehicleService;
import com.vehicelManagement.vehicelManagement.service.WebServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/webRequestController/api/v1")
@CrossOrigin("*")
public class WebApiController {

    @Autowired
    private WebServices webservices;

    @GetMapping("/getvehicles")
    public List<WebVehicleResponse> getVehicles() {
        return webservices.getAllVehicles();
    }
}
