package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.systemReponse.PaymentResponseDto;
import com.BookingService.BookingService.dto.BookingResponseDto;
import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.PaymentRepository;
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
    private final PaymentRepository paymentRepository;

    public TourController(TripRepository tripRepository, BookingRepository bookingRepository, PaymentRepository paymentRepository) {
        this.tripRepository = tripRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    @GetMapping("/user-dashboard/{touristId}")
    public ResponseEntity<List<BookingResponseDto>> getMyBookings(@PathVariable Long touristId) {
        // 1. Fetch user-specific bookings
        List<Booking> userBookings = bookingRepository.findByTouristId(touristId);
        List<BookingResponseDto> dashboardData = new ArrayList<>();

        for (Booking b : userBookings) {
            // 2. Business Logic: isDriverConfirm (true) -> "Confirmed", else -> "Pending"
            String drvStatus = (b.getIsDriverConfirm() != null && b.getIsDriverConfirm()) ? "Confirmed" : "Pending";

            // 3. Fetch Payment Status from PaymentRepository
            // Default to "Pending" if no payment record is found yet
            String payStatus = paymentRepository.findByBookingId(b.getBookingId())
                    .map(payment -> payment.getStatus()) // PAID, ADVANCED, etc.
                    .orElse("Pending");

            // 4. Link with Trip data to get dates and locations
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

                // NEW: Set the Payment Status for the UI
                dto.setPaymentStatus(payStatus);

                // Create route list for the "Tour Package" column
                dto.setRoute(Arrays.asList(t.getStartLocation(), t.getEndLocation()));

                dashboardData.add(dto);
            });
        }
        return ResponseEntity.ok(dashboardData);
    }
    /**
     * NEW: For the "My Payments" tab
     */
    @GetMapping("/user-payments/{touristId}")
    public ResponseEntity<List<PaymentResponseDto>> getMyPayments(@PathVariable Long touristId) {
        // 1. Get all bookings belonging to this tourist
        List<Booking> userBookings = bookingRepository.findByTouristId(touristId);
        List<PaymentResponseDto> paymentHistory = new ArrayList<>();

        for (Booking b : userBookings) {
            // 2. Look for a payment record linked to this bookingId
            paymentRepository.findByBookingId(b.getBookingId()).ifPresent(p -> {
                PaymentResponseDto response = new PaymentResponseDto();

                // Set data from Payment model
                // Format numeric ID to "TXN001" style
                response.setTransactionId("TXN" + String.format("%03d", p.getPaymentId()));
                response.setDate(p.getPaymentDateTime());
                response.setMethod(p.getPaymentType());
                response.setAmount(p.getPaidAmount());
                response.setStatus(p.getStatus());

                // 3. Fetch Trip info to get the "Description" for the UI
                tripRepository.findByTripId(b.getTripId()).ifPresent(t -> {
                    response.setDescription(t.getStartLocation() + " Tour");
                });

                paymentHistory.add(response);
            });
        }
        return ResponseEntity.ok(paymentHistory);
    }

}