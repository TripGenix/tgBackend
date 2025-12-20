package com.vehicelManagement.vehicelManagement.controller;

import com.vehicelManagement.vehicelManagement.dto.VehicleDto;
import com.vehicelManagement.vehicelManagement.dto.VehicleSaveDto;
import com.vehicelManagement.vehicelManagement.dto.VehicleUpdateDto;
import com.vehicelManagement.vehicelManagement.model.Vehicle;
import com.vehicelManagement.vehicelManagement.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/vehicleController/api/v1")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/getallvehicles")
    public List<VehicleDto> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/getvehiclebynumber")
    public ResponseEntity<?> getVehicleByNumber(@RequestParam String number) {
        try {
            return ResponseEntity.ok(vehicleService.getVehicleByNumber(number));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveVehicle(@RequestBody VehicleSaveDto vehicleDto) {

        try {
            VehicleDto savedVehicle = vehicleService.createVehicle(vehicleDto);
            return ResponseEntity.ok(savedVehicle);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateVehicle(
            @PathVariable int id,
            @RequestBody VehicleUpdateDto dto) {

        try {
            return ResponseEntity.ok(vehicleService.updateVehicle(id, dto));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable int id) {
        try {
            int deletedId = vehicleService.deleteVehicle(id);
            return ResponseEntity.ok("Vehicle deleted successfully. ID: " + deletedId);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/detailsOfVehicle/{id}")
    public ResponseEntity<?> getVehicleDetails(@PathVariable int id) {
        try {
            return ResponseEntity.ok(vehicleService.getVehicleDetails(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }



}
