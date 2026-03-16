package com.driverManagement.DriverManagement.repository;

import com.driverManagement.DriverManagement.models.Booking;
import com.driverManagement.DriverManagement.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    Trip findByTripId(int tripId);
}
