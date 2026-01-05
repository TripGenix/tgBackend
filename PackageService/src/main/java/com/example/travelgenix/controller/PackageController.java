package com.example.travelgenix.controller;

import com.example.travelgenix.DTO.PackageDto;
import com.example.travelgenix.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/packages")
@CrossOrigin("*")
public class PackageController {

    @Autowired private PackageService packageService;

    @PostMapping("/save")
    public PackageDto createPackage(@RequestBody PackageDto dto) {
        return packageService.savePackage(dto);
    }

    @GetMapping("/getall")
    public List<PackageDto> getAll() {
        return packageService.getAllPackages();
    }
    // --- NEW: UPDATE ENDPOINT ---
    @PutMapping("/update/{id}")
    public PackageDto updatePackage(@PathVariable Integer id, @RequestBody PackageDto dto) {
        return packageService.updatePackage(id, dto);
    }

    // --- NEW: DELETE ENDPOINT ---
    @DeleteMapping("/delete/{id}")
    public String deletePackage(@PathVariable Integer id) {
        return packageService.deletePackage(id);
    }
    @GetMapping("/{id}")
    public PackageDto getOne(@PathVariable Integer id) {
        return packageService.getPackageById(id);
    }
}