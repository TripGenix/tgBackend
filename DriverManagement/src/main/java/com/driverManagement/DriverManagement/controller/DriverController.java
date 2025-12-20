package com.driverManagement.DriverManagement.controller;

import com.driverManagement.DriverManagement.Dto.DriverSaveDto;
import com.driverManagement.DriverManagement.Dto.DriverUpdateDto;
import com.driverManagement.DriverManagement.Dto.DriverResponseDto;
import com.driverManagement.DriverManagement.services.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("driveController/api/v1")
public class DriverController {

    @Autowired
    private DriverService driverService;

    // GET ALL DRIVERS
    @GetMapping
    public ResponseEntity<List<DriverResponseDto>> getAllDrivers() {
        List<DriverResponseDto> drivers = driverService.getAllDrivers();
        return ResponseEntity.ok(drivers);
    }

    // SAVE DRIVER
    @PostMapping
    public ResponseEntity<DriverResponseDto> saveDriver(@RequestBody DriverSaveDto dto) {
        DriverResponseDto saved = driverService.saveDriver(dto);
        return ResponseEntity.ok(saved);
    }

    // UPDATE DRIVER
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponseDto> updateDriver(
            @RequestBody DriverUpdateDto dto,
            @PathVariable int id
    ) {
        DriverResponseDto updated = driverService.updateDriver(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE DRIVER (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDriver(@PathVariable int id) {
        try{
            driverService.deleteDriver(id);
            return ResponseEntity.ok("Driver deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // GET DRIVER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getDriver(@PathVariable int id) {

        try {
            return ResponseEntity.ok(driverService.getDriverById(id));
        }catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
