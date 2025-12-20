package com.vehicelManagement.vehicelManagement.repo;

import com.vehicelManagement.vehicelManagement.model.Owner;
import com.vehicelManagement.vehicelManagement.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleOwnerRepository extends JpaRepository<Owner, Integer> {
}
