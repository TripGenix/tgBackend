package com.example.tour_guide.services;

import com.example.tour_guide.dto.req.TourGuideDTO;
import com.example.tour_guide.model.TourGuide;
import com.example.tour_guide.repositories.TourGuideRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TourGuideService {

    private final TourGuideRepo repo;

    public TourGuideService(TourGuideRepo repo) {
        this.repo = repo;
    }

    // ✅ CREATE
    public String createGuide(TourGuideDTO dto) {
        TourGuide g = mapToEntity(dto);
        repo.save(g);
        return g.getName();
    }

    // ✅ GET ALL
    public List<TourGuideDTO> getAllTourGuides() {
        return repo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // ✅ SEARCH
    public TourGuideDTO searchGuide(Long id) {
        return repo.findById(id)
                .map(this::mapToDTO)
                .orElse(null);
    }

    // ✅ DELETE
    public void deleteGuide(Long id) {
        repo.deleteById(id);
    }

    // ✅ UPDATE
    public String updateGuide(TourGuideDTO dto) {

        if (dto.getTourGuideId() == null) {
            throw new RuntimeException("Tour guide ID is required for update");
        }

        TourGuide guide = repo.findById(dto.getTourGuideId())
                .orElseThrow(() -> new RuntimeException("Guide not found"));

        // update fields
        guide.setName(dto.getName());
        guide.setNic(dto.getNic());
        guide.setDescription(dto.getDescription());
        guide.setPricePerDay(dto.getPricePerDay());
        guide.setContactNumber(dto.getContactNumber());
        guide.setImage(dto.getImage());
        guide.setExperienceYears(dto.getExperienceYears());
        guide.setLanguages(dto.getLanguages());

        repo.save(guide);

        return "Guide updated successfully";
    }

    // ✅ ENTITY → DTO
    private TourGuideDTO mapToDTO(TourGuide g) {
        return new TourGuideDTO(
                g.getTourGuideId(),
                g.getName(),
                g.getNic(),
                g.getDescription(),
                g.getPricePerDay(),
                g.getContactNumber(),
                g.getImage(),
                g.getExperienceYears(),
                g.getLanguages()
        );
    }

    // ✅ DTO → ENTITY
    private TourGuide mapToEntity(TourGuideDTO dto) {
        TourGuide g = new TourGuide();

        g.setName(dto.getName());
        g.setNic(dto.getNic());
        g.setDescription(dto.getDescription());
        g.setPricePerDay(dto.getPricePerDay());
        g.setContactNumber(dto.getContactNumber());
        g.setImage(dto.getImage());
        g.setExperienceYears(dto.getExperienceYears());
        g.setLanguages(dto.getLanguages());

        return g;
    }
}