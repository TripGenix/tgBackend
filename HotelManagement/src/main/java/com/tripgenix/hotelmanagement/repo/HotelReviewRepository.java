package com.tripgenix.hotelmanagement.repo;

import com.tripgenix.hotelmanagement.model.HotelReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelReviewRepository extends JpaRepository<HotelReview, Integer> {

    // This allows you to get all reviews for a specific hotel ID if needed later
    List<HotelReview> findByHotel_Id(Integer hotelId);
}