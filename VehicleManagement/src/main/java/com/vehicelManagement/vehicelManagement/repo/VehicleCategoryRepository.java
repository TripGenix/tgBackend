package com.vehicelManagement.vehicelManagement.repo;

import com.vehicelManagement.vehicelManagement.dto.VehicleCategoryDto;
import com.vehicelManagement.vehicelManagement.model.VehicleCategory;
import com.vehicelManagement.vehicelManagement.model.VehicleImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleCategoryRepository extends JpaRepository<VehicleCategory, Integer> {
}
