package com.driverManagement.DriverManagement.services;

import com.driverManagement.DriverManagement.Dto.DriverBlockDateDto;
import com.driverManagement.DriverManagement.Dto.DriverResponseDto;
import com.driverManagement.DriverManagement.Dto.DriverSaveDto;
import com.driverManagement.DriverManagement.Dto.DriverUpdateDto;
import com.driverManagement.DriverManagement.models.Driver;
import com.driverManagement.DriverManagement.models.DriverBlockedDate;
import com.driverManagement.DriverManagement.repository.DriverBlockedDateRepository;
import com.driverManagement.DriverManagement.repository.DriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DriverBlockedDateRepository blockedDateRepository;
    @Transactional
    public String blockDriverDates(DriverBlockDateDto dto) {
        Driver driver = driverRepository.findById(dto.getDriverId())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        int addedCount = 0;

        if (dto.getBlockedDates() != null) {
            for (LocalDate date : dto.getBlockedDates()) {
                // Check if date already exists to avoid duplicates
                if (!blockedDateRepository.existsByDriver_DriverIdAndBlockedDate(dto.getDriverId(), date)) {
                    DriverBlockedDate blockedDate = new DriverBlockedDate();
                    blockedDate.setDriver(driver);
                    blockedDate.setBlockedDate(date);
                    blockedDate.setReason("Unavailable"); // Default reason
                    blockedDateRepository.save(blockedDate);
                    addedCount++;
                }
            }
        }
        return addedCount + " dates blocked successfully.";
    }

    public List<LocalDate> getDriverBlockedDates(int driverId) {
        List<DriverBlockedDate> entities = blockedDateRepository.findByDriver_DriverId(driverId);
        List<LocalDate> dates = new ArrayList<>();
        for (DriverBlockedDate entity : entities) {
            dates.add(entity.getBlockedDate());
        }
        return dates;
    }

    @Transactional
    public void clearBlockedDates(int driverId, LocalDate startDate, LocalDate endDate) {
        blockedDateRepository.deleteByDriverAndDateRange(driverId, startDate, endDate);
    }

    @Transactional
    public DriverResponseDto saveDriver(DriverSaveDto dto) {

        // 1. Map DTO → Entity & Save
        Driver driver = modelMapper.map(dto, Driver.class);
        driver.setIsDelete(false);
        Driver savedDriver = driverRepository.save(driver);
        int driverId = savedDriver.getDriverId();

        // 2. Insert Allocations (Vehicles)
        if (dto.getSelectedVehicleByNumber() != null) {
            for (Integer vehicleId : dto.getSelectedVehicleByNumber()) {
                driverRepository.insertDriverAllocatedVehicle(driverId, vehicleId);
            }
        }

        // 3. Insert Allocations (Categories)
        if (dto.getSelectedVehicleCategories() != null) {
            for (Integer categoryId : dto.getSelectedVehicleCategories()) {
                driverRepository.insertDriverAllocatedVehicleCategory(driverId, categoryId);
            }
        }

        // 4. Create Response & 🔴 POPULATE THE MISSING LISTS
        DriverResponseDto response = modelMapper.map(savedDriver, DriverResponseDto.class);

        // Fix: Set the lists manually from the input DTO (since we just saved them)
        response.setSelectedVehicleByNumber(dto.getSelectedVehicleByNumber());
        response.setSelectedVehicleCategories(dto.getSelectedVehicleCategories());

        return response;
    }
    @Transactional
    public DriverResponseDto updateDriver(int driverId, DriverUpdateDto dto) {

        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        modelMapper.map(dto, driver);

        Driver updatedDriver = driverRepository.save(driver);


        driverRepository.deleteAllocatedVehicles(driverId);

        if (dto.getSelectedVehicleByNumber() != null) {
            for (Integer vehicleId : dto.getSelectedVehicleByNumber()) {
                driverRepository.insertDriverAllocatedVehicle(driverId, vehicleId);
            }
        }

        driverRepository.deleteAllocatedCategories(driverId);

        if (dto.getSelectedVehicleCategories() != null) {
            for (Integer categoryId : dto.getSelectedVehicleCategories()) {
                driverRepository.insertDriverAllocatedVehicleCategory(driverId, categoryId);
            }
        }

        DriverResponseDto response = modelMapper.map(updatedDriver, DriverResponseDto.class);

        response.setSelectedVehicleByNumber(driverRepository.findAllocatedVehicles(driverId));
        response.setSelectedVehicleCategories(driverRepository.findAllocatedCategories(driverId));

        return response;
    }

    public DriverResponseDto getDriverById(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        DriverResponseDto dto = modelMapper.map(driver, DriverResponseDto.class);

        // Fetch allocated vehicle IDs
        List<Integer> allocatedVehicles = driverRepository.findAllocatedVehicles(driverId);
        dto.setSelectedVehicleByNumber(allocatedVehicles);

        // Fetch allocated category IDs
        List<Integer> allocatedCategories = driverRepository.findAllocatedCategories(driverId);
        dto.setSelectedVehicleCategories(allocatedCategories);

        return dto;
    }

    public List<DriverResponseDto> getAllDrivers() {
        List<Driver> drivers = driverRepository.getAllDrivers();

        List<DriverResponseDto> dtos = new ArrayList<>();

        for (Driver driver : drivers) {

            // Map base fields
            DriverResponseDto dto = modelMapper.map(driver, DriverResponseDto.class);

            // Fetch allocated vehicles
            List<Integer> allocatedVehicles = driverRepository.findAllocatedVehicles(driver.getDriverId());
            dto.setSelectedVehicleByNumber(allocatedVehicles);

            // Fetch allocated categories
            List<Integer> allocatedCategories = driverRepository.findAllocatedCategories(driver.getDriverId());
            dto.setSelectedVehicleCategories(allocatedCategories);

            dtos.add(dto);
        }

        return dtos;
    }

    public void deleteDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setIsDelete(true);
        driverRepository.save(driver);
    }

    @Transactional
    public void updateFCMToken(int driverId, String fcmToken) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setFcmToken(fcmToken);
        driverRepository.save(driver);
    }

    public String getFCMToken(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        return driver.getFcmToken();
    }

    public ResponseEntity<Driver> approveDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        driver.setApproved(true);
        driverRepository.save(driver);

        return ResponseEntity.ok(driver);
    }

    public List<DriverResponseDto> getApprovedDrivers() {
        List<Driver> drivers = driverRepository.findByApproved(true);

        List<DriverResponseDto> dtos = new ArrayList<>();

        for (Driver driver : drivers) {

            // Map base fields
            DriverResponseDto dto = modelMapper.map(driver, DriverResponseDto.class);

            // Fetch allocated vehicles
            List<Integer> allocatedVehicles = driverRepository.findAllocatedVehicles(driver.getDriverId());
            dto.setSelectedVehicleByNumber(allocatedVehicles);

            // Fetch allocated categories
            List<Integer> allocatedCategories = driverRepository.findAllocatedCategories(driver.getDriverId());
            dto.setSelectedVehicleCategories(allocatedCategories);

            dtos.add(dto);
        }

        return dtos;
    }
}
