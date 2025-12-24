package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository  extends JpaRepository<Route, Long> {
    @Query("SELECT r.wayPoint FROM Route r WHERE r.tripId = :tripId")
    List<String> findWayPointsByTripId(@Param("tripId") Long tripId);
}
