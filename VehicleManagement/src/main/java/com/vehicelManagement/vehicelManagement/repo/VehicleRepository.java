package com.vehicelManagement.vehicelManagement.repo;

import com.vehicelManagement.vehicelManagement.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
    @Query(value = "select * from vehicle where number_plate=?1 AND is_delete=false", nativeQuery = true)
    Optional<Vehicle> getVehicleByNumberPlate(String numberPlate);

    @Query(value = "select * from vehicle where is_delete=false", nativeQuery = true)
    List<Vehicle> getAllVehicles();

    @Query(value = "select * from vehicle where vehicle_id=?1 and is_delete=false", nativeQuery = true)
    Optional<Vehicle> getVehicleByVehicleId(Integer vehicleId);


}
