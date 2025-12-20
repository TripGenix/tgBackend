package com.driverManagement.DriverManagement.repository;

import com.driverManagement.DriverManagement.Dto.DriverResponseDto;
import com.driverManagement.DriverManagement.models.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Integer> {
    @Query(value = "select * from drivers where is_delete=false", nativeQuery = true)
    List<Driver> getAllDrivers();

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO driver_allocated_vehicle (driver_id, vehicle_id) VALUES (:driverId, :vehicleId)",
            nativeQuery = true
    )
    void insertDriverAllocatedVehicle(int driverId, int vehicleId);

    @Transactional
    @Modifying
    @Query(
            value = "INSERT INTO driver_allocated_vehicle_category (driver_id, category_id) VALUES (:driverId, :categoryId)",
            nativeQuery = true
    )
    void insertDriverAllocatedVehicleCategory(int driverId, int categoryId);

    @Query(
            value = "SELECT category_id FROM driver_allocated_vehicle_category WHERE driver_id = :driverId",
            nativeQuery = true
    )
    List<Integer> findAllocatedCategories(int driverId);

    @Query(
            value = "SELECT vehicle_id FROM driver_allocated_vehicle WHERE driver_id = :driverId",
            nativeQuery = true
    )
    List<Integer> findAllocatedVehicles(int driverId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM driver_allocated_vehicle WHERE driver_id = :driverId", nativeQuery = true)
    void deleteAllocatedVehicles(int driverId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM driver_allocated_vehicle_category WHERE driver_id = :driverId", nativeQuery = true)
    void deleteAllocatedCategories(int driverId);

}
