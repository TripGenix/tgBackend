package com.tripgenix.hotelmanagement.repo;

import com.tripgenix.hotelmanagement.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    // Basic CRUD is built-in
}