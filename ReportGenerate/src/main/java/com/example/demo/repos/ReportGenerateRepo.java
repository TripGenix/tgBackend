package com.example.demo.repos;

import com.example.demo.models.Earnings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportGenerateRepo extends JpaRepository<Earnings, Long> {

    List<Earnings> findByPaymentDateTimeBetween(
            LocalDateTime start,
            LocalDateTime end
    );
}