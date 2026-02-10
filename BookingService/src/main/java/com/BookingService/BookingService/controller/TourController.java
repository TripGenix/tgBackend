package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.BookingResponseDto;
import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.TripRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/booking")
public class TourController {

    private final TripRepository tripRepository;
    private final BookingRepository bookingRepository;

    public TourController(TripRepository tripRepository, BookingRepository bookingRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/user-dashboard/{touristId}")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(@PathVariable Long touristId) {
        // 1. Fetch user-specific bookings
        List<Booking> userBookings = bookingRepository.findByTouristId(touristId);
        List<BookingResponseDto> dashboardData = new ArrayList<>();

        for (Booking b : userBookings) {
            // 2. Business Logic: isDriverConfirm (true) -> "Confirmed", else -> "Pending"
            String drvStatus = b.getIsDriverConfirm() ? "Confirmed" : "Pending";

            // 3. Link with Trip data to get dates and locations
            tripRepository.findByTripId(b.getTripId()).ifPresent(t -> {
                BookingResponseDto dto = new BookingResponseDto();

                // Set data from Booking model
                dto.setBookingId(b.getBookingId());
                dto.setReferenceId(b.getReferenceId());
                dto.setCustomerName(b.getBookerName());
                dto.setTouristId(b.getTouristId());
                dto.setCreatedAt(b.getDateCreated());

                // Set data from Trip model
                dto.setTripId(t.getTripId());
                dto.setStartDate(t.getStartDateTime());
                dto.setEndDate(t.getEndDateTime());
                dto.setStatus(t.getStatus());

                // Set the mapped Driver Status
                dto.setDriverStatus(drvStatus);

                // Create route list for the "Tour Package" column
                dto.setRoute(Arrays.asList(t.getStartLocation(), t.getEndLocation()));

                dashboardData.add(dto);
            });
        }
        return ResponseEntity.ok(dashboardData);
    }
}