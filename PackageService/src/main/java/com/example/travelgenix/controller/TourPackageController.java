package com.example.travelgenix.controller;

import com.example.travelgenix.DTO.TourPackageDto;
import com.example.travelgenix.service.TourPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@CrossOrigin("*")
public class TourPackageController {

    @Autowired
    private TourPackageService tourPackageService;

    // GET ALL PACKAGES
    @GetMapping
    public ResponseEntity<List<TourPackageDto>> getAllPackages() {
        List<TourPackageDto> packages = tourPackageService.getAll();
        return ResponseEntity.ok(packages);
    }

    // GET PACKAGE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getPackageById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tourPackageService.getById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // SAVE PACKAGE
    @PostMapping
    public ResponseEntity<?> savePackage(@RequestBody TourPackageDto dto) {
        try {
            return ResponseEntity.ok(tourPackageService.save(dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UPDATE PACKAGE
    @PutMapping("/{id}")
    public ResponseEntity<?> updatePackage(
            @PathVariable Long id,
            @RequestBody TourPackageDto dto) {
        try{
            TourPackageDto updated = tourPackageService.update(id, dto);
            return ResponseEntity.ok(updated);
    }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // DELETE PACKAGE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePackage(@PathVariable Long id) {
        try {
            tourPackageService.delete(id);
            return ResponseEntity.ok("Package deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
