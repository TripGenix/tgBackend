package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.BookingRequestDto;
import com.BookingService.BookingService.dto.BookingResponseDto;
import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseDto;
import com.BookingService.BookingService.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("bookingservice/api/v1")
@CrossOrigin("*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping("get_all_bookings")
    public ResponseEntity<List<BookingSystemResponseDto>> bookingService() {
        List<BookingSystemResponseDto> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("get_new_bookings")
    public ResponseEntity<List<BookingSystemResponseDto>> newBooking() {
        List<BookingSystemResponseDto> bookings = bookingService.getNewBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("get_passenger_confirm_bookings")
    public String passengerConfirmBooking() {
        return "Booking Service is running";
    }

    @GetMapping("get_driver_pending_bookings")
    public String driverPendingBooking() {
        return "Booking Service is running";
    }

    @GetMapping("get_ongoing_bookings")
    public String ongoingBooking() {
        return "Booking Service is running";
    }

    @GetMapping("get_finished_bookings")
    public String finishedBooking() {
        return "Booking Service is running";
    }

    @GetMapping("get_cancelled_bookings")
    public String cancelledBooking() {
        return "Booking Service is running";
    }

    @PostMapping("/saveBooking")
    public ResponseEntity<BookingResponseDto> bookingServicePost(
            @RequestBody BookingRequestDto dto
    ) {
        BookingResponseDto booking = bookingService.createBooking(dto);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

}
