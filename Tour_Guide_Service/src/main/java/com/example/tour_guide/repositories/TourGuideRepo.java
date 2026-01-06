package com.example.tour_guide.repositories;

import com.example.tour_guide.model.TourGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TourGuideRepo extends JpaRepository<TourGuide,Long> {


}
