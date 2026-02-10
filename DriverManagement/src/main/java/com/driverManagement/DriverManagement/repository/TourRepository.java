package com.driverManagement.DriverManagement.repository;

import com.driverManagement.DriverManagement.models.Booking;
import com.driverManagement.DriverManagement.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByDriverIdAndIsDriverConfirmAndIsDriverCancelled(int driverId,boolean isDriverConfirm, boolean isDriverCancelled);


}
