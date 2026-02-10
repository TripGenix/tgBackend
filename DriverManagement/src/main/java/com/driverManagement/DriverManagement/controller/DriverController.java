package com.driverManagement.DriverManagement.controller;

import com.driverManagement.DriverManagement.Dto.DriverSaveDto;
import com.driverManagement.DriverManagement.Dto.DriverUpdateDto;
import com.driverManagement.DriverManagement.Dto.DriverResponseDto;
import com.driverManagement.DriverManagement.models.Driver;
import com.driverManagement.DriverManagement.Dto.*;
import com.driverManagement.DriverManagement.services.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @GetMapping("/get-approved-drivers")
    public ResponseEntity<List<DriverResponseDto>> getApprovedDrivers() {
        List<DriverResponseDto> drivers = driverService.getApprovedDrivers();
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

    // UPDATE FCM TOKEN (for Flutter app)
    @PutMapping("/{id}/fcm-token")
    public ResponseEntity<?> updateFCMToken(
            @PathVariable int id,
            @RequestBody FCMTokenUpdateDto dto) {
        try {
            driverService.updateFCMToken(id, dto.getFcmToken());
            return ResponseEntity.ok("FCM token updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // GET FCM TOKEN (for NotificationService)
    @GetMapping("/{id}/fcm-token")
    public ResponseEntity<?> getFCMToken(@PathVariable int id) {
        try {
            String fcmToken = driverService.getFCMToken(id);
            return ResponseEntity.ok(fcmToken);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/block-dates")
    public ResponseEntity<?> blockDates(@RequestBody DriverBlockDateDto dto) {
        try {
            String message = driverService.blockDriverDates(dto);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 2. Get Blocked Dates (So the Calendar shows them when app reloads)
    @GetMapping("/{id}/blocked-dates")
    public ResponseEntity<List<LocalDate>> getBlockedDates(@PathVariable int id) {
        return ResponseEntity.ok(driverService.getDriverBlockedDates(id));
    }

    // 3. Clear Blocked Dates (For the "Clear" button in Flutter)
    // Query params: ?startDate=2024-02-01&endDate=2024-02-29
    @DeleteMapping("/{id}/block-dates")
    public ResponseEntity<?> clearBlockedDates(
            @PathVariable int id,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        try {
            driverService.clearBlockedDates(id, startDate, endDate);
            return ResponseEntity.ok("Dates cleared successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/approve-driver/{driverId}")
    public ResponseEntity<Driver> approveDriver(@PathVariable int driverId) {
        return driverService.approveDriver(driverId);
    }


}
