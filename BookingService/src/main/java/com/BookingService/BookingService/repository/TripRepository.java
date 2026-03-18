package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    // Fetch all bookings for a specific tourist
    List<Trip> findByTouristId(Long touristId);

    // Fetch a specific booking by its ID
    Optional<Trip> findByTripId(Long tripId);
}
