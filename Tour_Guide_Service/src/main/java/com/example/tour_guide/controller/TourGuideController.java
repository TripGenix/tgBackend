package com.example.tour_guide.controller;

import com.example.tour_guide.dto.req.TourGuideDTO;
import com.example.tour_guide.services.TourGuideService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
//@CrossOrigin("*")
public class TourGuideController {

    private final TourGuideService service;

    public TourGuideController(TourGuideService service) {
        this.service = service;
    }

    @PostMapping("/create-tour-guide")
    public ResponseEntity<String> create(@RequestBody TourGuideDTO dto) {
        try {
            String result = service.createGuide(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating guide: " + e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<TourGuideDTO>> getAll() {
        try {
            List<TourGuideDTO> guides = service.getAllTourGuides();
            return ResponseEntity.ok(guides);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(path="/search", params="tourId")
    public ResponseEntity<TourGuideDTO> search(@RequestParam Long tourId) {
        try {
            TourGuideDTO guide = service.searchGuide(tourId);
            if (guide == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(guide);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update-tour-guide")
    public ResponseEntity<String> update(@RequestBody TourGuideDTO dto) {
        try {
            String result = service.updateGuide(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating guide: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{tourId}")
    public ResponseEntity<String> delete(@PathVariable Long tourId) {
        try {
            service.deleteGuide(tourId);
            return ResponseEntity.ok("Guide deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting guide: " + e.getMessage());
        }
    }
}