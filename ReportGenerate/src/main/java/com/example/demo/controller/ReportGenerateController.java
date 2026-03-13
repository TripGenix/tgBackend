package com.example.demo.controller;

import com.example.demo.models.Earnings;
import com.example.demo.services.reportGenerateService;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/reportgenerate")
@CrossOrigin("*")
public class ReportGenerateController {

    private final reportGenerateService reportService;

    public ReportGenerateController(reportGenerateService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/earnings")
    public Map<String, Object> getEarnings(
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {

        LocalDateTime start = null;
        LocalDateTime end = null;

        if (from != null && to != null) {
            start = LocalDateTime.parse(from + "T00:00:00");
            end = LocalDateTime.parse(to + "T23:59:59");
        }

        List<Earnings> earnings = reportService.getEarnings(start, end);

        BigDecimal total = reportService.calculateTotal(earnings);

        Map<String, Object> response = new HashMap<>();

        response.put("totalEarnings", total);
        response.put("data", earnings);

        return response;
    }
}