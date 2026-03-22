package com.example.tour_guide.controller;

import com.example.tour_guide.dto.req.TourGuideDTO;
import com.example.tour_guide.services.TourGuideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
//@CrossOrigin(origins = "*")
public class TourGuideController {

    private final TourGuideService service;

    public TourGuideController(TourGuideService service) {
        this.service = service;
    }

    // ✅ CREATE (same endpoint)
    @PostMapping("/create-tour-guide")
    public ResponseEntity<String> create(@RequestBody TourGuideDTO dto) {
        String result = service.createGuide(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    // ✅ GET ALL (same endpoint)
    @GetMapping("/getAll")
    public ResponseEntity<List<TourGuideDTO>> getAll() {
        return ResponseEntity.ok(service.getAllTourGuides());
    }

    // ✅ SEARCH (same endpoint)
    @GetMapping(path = "/search", params = "tourId")
    public ResponseEntity<TourGuideDTO> search(@RequestParam Long tourId) {

        TourGuideDTO guide = service.searchGuide(tourId);

        if (guide == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(guide);
    }

    // ✅ UPDATE (same endpoint)
    @PutMapping("/update-tour-guide")
    public ResponseEntity<String> update(@RequestBody TourGuideDTO dto) {

        if (dto.getTourGuideId() == null) {
            return ResponseEntity.badRequest().body("TourGuideId is required");
        }

        String result = service.updateGuide(dto);
        return ResponseEntity.ok(result);
    }

    // ✅ DELETE (same endpoint)
    @DeleteMapping("/delete/{tourId}")
    public ResponseEntity<String> delete(@PathVariable Long tourId) {
        service.deleteGuide(tourId);
        return ResponseEntity.ok("Guide deleted successfully");
    }
}