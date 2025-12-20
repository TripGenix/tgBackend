package com.vehicelManagement.vehicelManagement.service;

import com.vehicelManagement.vehicelManagement.dto.*;
import com.vehicelManagement.vehicelManagement.model.Owner;
import com.vehicelManagement.vehicelManagement.model.Vehicle;
import com.vehicelManagement.vehicelManagement.model.VehicleImage;
import com.vehicelManagement.vehicelManagement.repo.VehicleImageRepository;
import com.vehicelManagement.vehicelManagement.repo.VehicleOwnerRepository;
import com.vehicelManagement.vehicelManagement.repo.VehicleRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private VehicleImageRepository vehicleImageRepository;

    @Autowired
    private VehicleOwnerRepository ownerRepository;

    //get all
    public List<VehicleDto> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();
        return modelMapper.map(vehicles, new TypeToken<List<VehicleDto>>() {}.getType());
    }

    //save vehicle
    @Transactional
    public VehicleDto createVehicle(VehicleSaveDto dto) {

        // 1️⃣ Check duplicate number plate
        if (vehicleRepository.getVehicleByNumberPlate(dto.getVehicleNumber()).isPresent()) {
            throw new RuntimeException("Vehicle already exists!");
        }

        // 2️⃣ Create and save Owner
        Owner owner = new Owner();
        owner.setName(dto.getOwnerName());
        owner.setNic(dto.getOwnerId());
        owner.setPhone(dto.getPhone());
        owner.setAddressLine1(dto.getAddress1());
        owner.setAddressLine2(dto.getAddress2());
        owner.setStateProvince(dto.getState());
        owner.setPostalCode(dto.getPostalCode());
        owner.setDateOfBirth(dto.getDob());
        owner.setOwnerImage(dto.getOwnerImage());
        owner.setIsDelete(0);
        owner.setCreatedAt(LocalDateTime.now());

        owner = ownerRepository.save(owner);

        // 3️⃣ Create Vehicle
        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setNumberPlate(dto.getVehicleNumber());
        vehicle.setType(dto.getCategory());
        vehicle.setDescription(dto.getDescription());
        vehicle.setPassengerCount(dto.getPassengerCount());
        vehicle.setCostPerKm(dto.getCostPerKm());
        vehicle.setBookingPrice(dto.getBookingPrice());
        vehicle.setStatus(dto.getStatus());
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setIsDelete(false);
        vehicle.setOwner(owner);
        vehicle.setDocumentUrl(dto.getDocumentUrl());

        vehicle = vehicleRepository.save(vehicle);
        System.out.println(vehicle);

        if (dto.getVehicleImages() != null) {
            for (String url : dto.getVehicleImages()) {
                VehicleImage image = new VehicleImage();
                image.setVehicle(vehicle);
                image.setImageUrl(url);
                vehicleImageRepository.save(image);
            }
        }

        return modelMapper.map(vehicle, VehicleDto.class);
    }

    //get by number
    public VehicleDto getVehicleByNumber(String numberPlate) {
        Vehicle vehicle = vehicleRepository.getVehicleByNumberPlate(numberPlate)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return modelMapper.map(vehicle, VehicleDto.class);
    }



    //update vehicle
    @Transactional
    public VehicleDto updateVehicle(int id, VehicleUpdateDto dto) {

        // 1️⃣ Find vehicle
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // 2️⃣ Update owner details
        Owner owner = vehicle.getOwner();
        owner.setName(dto.getOwnerName());
        owner.setNic(dto.getOwnerId());
        owner.setPhone(dto.getPhone());
        owner.setAddressLine1(dto.getAddress1());
        owner.setAddressLine2(dto.getAddress2());
        owner.setStateProvince(dto.getState());
        owner.setPostalCode(dto.getPostalCode());

        // Convert DOB string → LocalDate
        if (dto.getDob() != null) {
            owner.setDateOfBirth(LocalDate.parse(dto.getDob()));
        }

        // Update owner image only if provided
        if (dto.getOwnerImage() != null) {
            owner.setOwnerImage(dto.getOwnerImage());
        }

        ownerRepository.save(owner);

        // 3️⃣ Update vehicle main fields
        vehicle.setVehicleName(dto.getVehicleName());
        vehicle.setNumberPlate(dto.getVehicleNumber());
        vehicle.setType(dto.getCategory());
        vehicle.setDescription(dto.getDescription());
        vehicle.setPassengerCount(dto.getPassengerCount());
        vehicle.setCostPerKm(BigDecimal.valueOf(dto.getCostPerKm()));
        vehicle.setBookingPrice(BigDecimal.valueOf(dto.getBookingPrice()));
        vehicle.setStatus(dto.getStatus());

        // Update PDF document
        if (dto.getDocumentUrl() != null) {
            vehicle.setDocumentUrl(dto.getDocumentUrl());
        }

        // 4️⃣ Update images (OPTION 2 – delete by vehicleId)
        if (dto.getVehicleImages() != null && !dto.getVehicleImages().isEmpty()) {

            // Delete all old images
            //vehicleImageRepository.deleteByVehicle_VehicleId(vehicle.getVehicleId());

            // Save new images
            for (String url : dto.getVehicleImages()) {
                VehicleImage image = new VehicleImage();
                image.setVehicle(vehicle);
                image.setImageUrl(url);
                vehicleImageRepository.save(image);
            }
        }

        // 5️⃣ Save vehicle
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);

        // 6️⃣ Return DTO
        return modelMapper.map(updatedVehicle, VehicleDto.class);
    }


    public int deleteVehicle(int id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        vehicle.setIsDelete(true);
        vehicleRepository.save(vehicle);
        return id;
    }

    public VehicleReciveDto getVehicleDetails(Integer id) {

        Vehicle vehicle = vehicleRepository.getVehicleByVehicleId(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        // Map vehicle to DTO
        VehicleReciveDto dto = modelMapper.map(vehicle, VehicleReciveDto.class);

        // Set owner DTO
        OwnerDto ownerDto = modelMapper.map(vehicle.getOwner(), OwnerDto.class);
        dto.setOwner(ownerDto);

        // Fetch all images
        List<String> images = vehicleImageRepository.findByVehicle(vehicle)
                .stream()
                .map(VehicleImage::getImageUrl)
                .toList();

        dto.setVehicleImages(images);

        return dto;
    }

}
