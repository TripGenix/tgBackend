package com.driverManagement.DriverManagement.repository;

import com.driverManagement.DriverManagement.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByDriverIdAndIsDriverConfirmAndIsDriverCancelled(int driverId,boolean isDriverConfirm, boolean isDriverCancelled);
}
