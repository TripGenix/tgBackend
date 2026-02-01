package com.driverManagement.DriverManagement.controller;

import com.driverManagement.DriverManagement.Dto.StartEndTourRequest;
import com.driverManagement.DriverManagement.Dto.TourStatusUpdateDto;
import com.driverManagement.DriverManagement.Dto.VerifyOtpRequest;
import com.driverManagement.DriverManagement.models.Booking;
import com.driverManagement.DriverManagement.services.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tour-controller/api/v1")
public class TourController {

    @Autowired
    TourService tourService;

    @GetMapping("/pending-tours/{driverId}")
    public List<Booking> pendingTours(@PathVariable int driverId) {
        return tourService.getPendingApprovedBookings(driverId);
    }

    @GetMapping("/cancled-tours/{driverId}")
    public List<Booking> cancledTours(@PathVariable int driverId) {
        return tourService.getcancledBookings(driverId);
    }

    @PostMapping("/confirm-tour/{tourId}")
    public TourStatusUpdateDto confirmTour(@PathVariable int tourId) {
        return tourService.confirmTour(tourId);
    }

    @PostMapping("/cancle-tour/{tourId}")
    public TourStatusUpdateDto cancleTour(@PathVariable int tourId) {
        return tourService.cancelTour(tourId);
    }

    @PostMapping("/{tourId}/request-start")
    public ResponseEntity<String> requestStart(@PathVariable int tourId) {
        tourService.requestTourStart(tourId);
        return ResponseEntity.ok("OTP sent to user");
    }

    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(
            @RequestBody VerifyOtpRequest request
    ) {
        tourService.verifyOtpAndStartTour(
                request.getTourId(),
                request.getOtp()
        );
        return ResponseEntity.ok("Tour started successfully");
    }

}
