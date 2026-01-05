package com.example.travelgenix.service;

import com.example.travelgenix.DTO.PackageDto;
import com.example.travelgenix.model.PackageActivity;
import com.example.travelgenix.model.TravelPackage;
import com.example.travelgenix.repository.PackageRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PackageService {

    @Autowired private PackageRepository packageRepository;
    @Autowired private ModelMapper modelMapper;

    @Transactional
    public PackageDto savePackage(PackageDto dto) {
        TravelPackage tPackage = modelMapper.map(dto, TravelPackage.class);

        // Fix Relationships for Activities
        if (tPackage.getActivities() != null) {
            for (PackageActivity activity : tPackage.getActivities()) {
                activity.setTravelPackage(tPackage);
            }
        }

        TravelPackage saved = packageRepository.save(tPackage);
        return convertToDto(saved);
    }

    // --- NEW: UPDATE METHOD ---
    @Transactional
    public PackageDto updatePackage(Integer id, PackageDto dto) {
        // 1. Find existing package
        TravelPackage existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found with id: " + id));

        // 2. Map new DTO values onto the EXISTING Entity
        // This updates fields like name, price, description, etc.
        modelMapper.map(dto, existingPackage);

        // Ensure ID stays correct (just in case DTO had null/different ID)
        existingPackage.setId(id);

        // 3. Re-link Activities (Crucial for Hibernate)
        // If the list of activities changed, we need to ensure the parent reference is set
        if (existingPackage.getActivities() != null) {
            for (PackageActivity activity : existingPackage.getActivities()) {
                activity.setTravelPackage(existingPackage);
            }
        }

        // 4. Save
        TravelPackage updated = packageRepository.save(existingPackage);
        return convertToDto(updated);
    }

    // --- NEW: DELETE METHOD ---
    public String deletePackage(Integer id) {
        if (!packageRepository.existsById(id)) {
            throw new RuntimeException("Package not found with id: " + id);
        }
        packageRepository.deleteById(id);
        return "Package deleted successfully";
    }

    public List<PackageDto> getAllPackages() {
        return packageRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PackageDto getPackageById(Integer id) {
        TravelPackage tPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        return convertToDto(tPackage);
    }

    private PackageDto convertToDto(TravelPackage tPackage) {
        PackageDto dto = modelMapper.map(tPackage, PackageDto.class);
        dto.setId(String.valueOf(tPackage.getId()));
        return dto;
    }
}