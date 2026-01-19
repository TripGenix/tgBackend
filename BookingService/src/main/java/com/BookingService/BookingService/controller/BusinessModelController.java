package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostRequestDto;
import com.BookingService.BookingService.service.BusinessModel.BusinessModelSercvice;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("businessmodel/api/v1")
@CrossOrigin("*")
public class BusinessModelController {

    private final BusinessModelSercvice businessModelSercvice;

    BusinessModelController(BusinessModelSercvice businessModelSercvice) {
        this.businessModelSercvice = businessModelSercvice;
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }

    @PostMapping("get_estimated_booking_cost")
    public Double getEstimatedBookingCost(@RequestBody  EstimatedCostRequestDto estimatedCostRequestDto) {
        return (businessModelSercvice.calculateEstimatedCost(estimatedCostRequestDto));
    }
}
