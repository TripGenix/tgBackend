package com.example.tour_guide.services;

import com.example.tour_guide.dto.req.TourGuideDTO;
import com.example.tour_guide.model.TourGuide;
import com.example.tour_guide.repositories.TourGuideRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TourGuideService {
    private final TourGuideRepo tourGuideRepo;

    public TourGuideService(TourGuideRepo tourGuideRepo) {
        this.tourGuideRepo = tourGuideRepo;
    }

    public String createGuide(TourGuideDTO tourGuideDTO) {
        System.out.println("createGuide tourGuideDTO = " + tourGuideDTO);
        TourGuide tourGuide = new TourGuide();
        tourGuide.setName(tourGuideDTO.getName());

        tourGuide.setLanguage(tourGuideDTO.getLanguage());
        tourGuide.setReviewId(tourGuideDTO.getReviewId());
        tourGuide.setImage(tourGuideDTO.getImage());
        tourGuide.setStatus(tourGuideDTO.isStatus());
        tourGuide.setNic(tourGuideDTO.getNic());
        tourGuide.setDriver(tourGuideDTO.isDriver());
        tourGuideRepo.save(tourGuide);
        return tourGuideDTO.getName();
    }


    public TourGuideDTO searchGuide(Long tourId) {
        if(tourGuideRepo.existsById(tourId)){
            TourGuide tourGuide=tourGuideRepo.getReferenceById(tourId);
            TourGuideDTO tourGuideDTO=new TourGuideDTO(
                    tourGuide.getLanguage(),
                    tourGuide.getReviewId(),
                    tourGuide.getImage(),
                    tourGuide.getName(),
                    tourGuide.isStatus(),
                    tourGuide.getNic(),
                    tourGuide.isDriver()
            );
            return tourGuideDTO;
        }else{
            System.out.println("There is not exists tourGuide");
            return null;
        }
    }

    public List<TourGuideDTO> getAllTourGuides() {
        List<TourGuide> getAllGuides = tourGuideRepo.findAll();
        List<TourGuideDTO> allTourGuideDTOS=new ArrayList<>();

        for(TourGuide tourGuide:getAllGuides){
            TourGuideDTO tourGuideDTO=new TourGuideDTO(
                    tourGuide.getLanguage(),
                    tourGuide.getReviewId(),
                    tourGuide.getImage(),
                    tourGuide.getName(),
                    tourGuide.isStatus(),
                    tourGuide.getNic(),
                    tourGuide.isDriver()
            );
            allTourGuideDTOS.add(tourGuideDTO);
        }
        return allTourGuideDTOS;
    }

    public String deleteGuide(Long tourId) {
        tourGuideRepo.deleteById(tourId);
        return tourId + " succesfully deleted";
    }
}
