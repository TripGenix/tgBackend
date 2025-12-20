package com.vehicelManagement.vehicelManagement.repo;

import com.vehicelManagement.vehicelManagement.model.Vehicle;
import com.vehicelManagement.vehicelManagement.model.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleImageRepository extends JpaRepository<VehicleImage, Integer> {
    List<VehicleImage> findByVehicle(Vehicle vehicle);

    void deleteByVehicle_VehicleId(Integer vehicleId);

}
