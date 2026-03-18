package com.driverManagement.DriverManagement.repository;

import com.driverManagement.DriverManagement.models.DriverBlockedDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DriverBlockedDateRepository extends JpaRepository<DriverBlockedDate, Integer> {

    // Find all blocked dates for a specific driver
    List<DriverBlockedDate> findByDriver_DriverId(int driverId);

    // Check if a specific date is already blocked to prevent duplicates
    boolean existsByDriver_DriverIdAndBlockedDate(int driverId, LocalDate date);

    // Delete specific dates (Used for the "Clear" button functionality)
    @Modifying
    @Query("DELETE FROM DriverBlockedDate d WHERE d.driver.driverId = :driverId AND d.blockedDate BETWEEN :startDate AND :endDate")
    void deleteByDriverAndDateRange(int driverId, LocalDate startDate, LocalDate endDate);
}