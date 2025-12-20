package com.vehicelManagement.vehicelManagement.service;

import com.vehicelManagement.vehicelManagement.dto.OwnerDto;
import com.vehicelManagement.vehicelManagement.dto.VehicleDto;
import com.vehicelManagement.vehicelManagement.dto.VehicleReciveDto;
import com.vehicelManagement.vehicelManagement.dto.WebVehicleResponse;
import com.vehicelManagement.vehicelManagement.model.Vehicle;
import com.vehicelManagement.vehicelManagement.model.VehicleImage;
import com.vehicelManagement.vehicelManagement.repo.VehicleImageRepository;
import com.vehicelManagement.vehicelManagement.repo.VehicleRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WebServices {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleImageRepository vehicleImageRepository;


    public List<WebVehicleResponse> getAllVehicles() {

        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();

        List<WebVehicleResponse> responseList = vehicles.stream()
                .map(vehicle -> {
                    WebVehicleResponse dto = modelMapper.map(vehicle, WebVehicleResponse.class);

                    List<String> images = vehicleImageRepository.findByVehicle(vehicle)
                            .stream()
                            .map(VehicleImage::getImageUrl)
                            .toList();

                    dto.setVehicleImages(images);

                    return dto;
                })
                .toList();

        return responseList;
    }
}
