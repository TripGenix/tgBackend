package com.example.demo.services;

import com.example.demo.models.Earnings;
import com.example.demo.repos.ReportGenerateRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class reportGenerateService {

    private final ReportGenerateRepo reportRepo;

    public reportGenerateService(ReportGenerateRepo reportRepo) {
        this.reportRepo = reportRepo;
    }

    public List<Earnings> getEarnings(LocalDateTime start, LocalDateTime end) {

        if (start != null && end != null) {
            return reportRepo.findByPaymentDateTimeBetween(start, end);
        }

        return reportRepo.findAll();
    }

    public BigDecimal calculateTotal(List<Earnings> earnings) {

        return earnings.stream()
                .map(Earnings::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}