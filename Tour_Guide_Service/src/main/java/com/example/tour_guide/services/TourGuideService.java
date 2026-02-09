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

    public String createGuide(TourGuideDTO dto) {
        TourGuide g = new TourGuide();
        g.setLanguage(dto.getLanguage());
        g.setReviewId(dto.getReviewId());
        g.setImage(dto.getImage());
        g.setName(dto.getName());
        g.setStatus(dto.isStatus());
        g.setNic(dto.getNic());
        g.setDriver(dto.getDriver());

        repo.save(g);
        return g.getName();
    }

    public List<TourGuideDTO> getAllTourGuides() {
        return repo.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    public TourGuideDTO searchGuide(Long id) {
        return repo.findById(id).map(this::map).orElse(null);
    }

    public void deleteGuide(Long id) {
        repo.deleteById(id);
    }

    private TourGuideDTO map(TourGuide g) {
        return new TourGuideDTO(
                g.getTourGuideId(),
                g.getLanguage(),
                g.getReviewId(),
                g.getImage(),
                g.getName(),
                g.isStatus(),
                g.getNic(),
                g.getDriver()
        );
    }
}
