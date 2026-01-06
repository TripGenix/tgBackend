package com.example.tour_guide.controller;

import com.example.tour_guide.dto.req.TourGuideDTO;
import com.example.tour_guide.services.TourGuideService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1")
public class TourGuideController {
    private final TourGuideService tourGuideService;

    public TourGuideController(TourGuideService tourGuideService) {
        this.tourGuideService = tourGuideService;
    }

    @PostMapping("/create-tour-guide")
    public String createTourGuide(@RequestBody TourGuideDTO tourGuideDTO) {
       String tourservice =  tourGuideService.createGuide(tourGuideDTO);
        return tourservice +" succesfully created";
    }

    @GetMapping(path = "/search" , params = "tourId")
    public TourGuideDTO searchTourGuide(@RequestParam Long tourId){
        TourGuideDTO tourGuideDTO1=tourGuideService.searchGuide(tourId);
        return tourGuideDTO1;
    }

    @GetMapping(path="/getAll")
    public List<TourGuideDTO> getAllTourGuide(){
        List<TourGuideDTO> allTourGuides=tourGuideService.getAllTourGuides();
        return allTourGuides;
    }

    @DeleteMapping(path="/delete")
    public String deleteTourGuide(@RequestParam Long tourId){
        tourGuideService.deleteGuide(tourId);
        return tourId + " succesfully deleted";
    }

}
