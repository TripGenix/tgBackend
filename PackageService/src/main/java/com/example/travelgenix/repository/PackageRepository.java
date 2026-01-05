package com.example.travelgenix.repository;

import com.example.travelgenix.model.TravelPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<TravelPackage, Integer> {
}