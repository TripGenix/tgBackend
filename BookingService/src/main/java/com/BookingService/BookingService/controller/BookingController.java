package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.ActionResponse;
import com.BookingService.BookingService.dto.BookingRequestDto;
import com.BookingService.BookingService.dto.BookingResponseDto;
import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostRequestDto;
import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostResponse;
import com.BookingService.BookingService.dto.ConfirmBookingEmailRequest;
import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseById;
import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseDto;
import com.BookingService.BookingService.service.BookingService;
import com.BookingService.BookingService.service.BusinessModel.BusinessModelSercvice;
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

    @Autowired
    private BusinessModelSercvice businessModelService;


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

    @GetMapping("get_confirmed_bookings")
    public ResponseEntity<List<BookingSystemResponseDto>> confirmedBooking() {
        List<BookingSystemResponseDto> bookings = bookingService.getConfirmedBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("get_driver_confirmed_bookings")
    public ResponseEntity<List<BookingSystemResponseDto>> driverConfirmedBooking() {
        List<BookingSystemResponseDto> bookings = bookingService.getDriverConfirmedBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("get_booking_by_id/{bookingId}")
    @ResponseBody
    public ResponseEntity<BookingSystemResponseById> getBookingById(@PathVariable Long bookingId) {
        BookingSystemResponseById  bookings = bookingService.getBookingById(bookingId);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/saveBooking")
    public ResponseEntity<BookingResponseDto> bookingServicePost(
            @RequestBody BookingRequestDto dto
    ) {
        BookingResponseDto booking = bookingService.createBooking(dto);
        return new ResponseEntity<>(booking, HttpStatus.CREATED);
    }

    @PostMapping("/send_confirm_booking_email/{bookingId}")
    public ResponseEntity<String> confirmBooking(@PathVariable Long bookingId) {
        bookingService.sendEmail(bookingId);
        return ResponseEntity.ok("Email Send Successfully");

    }

    @PostMapping("/get_estimated_cost")
    public ResponseEntity<EstimatedCostResponse> getEstimatedBookingCost(
            @RequestBody EstimatedCostRequestDto estimatedCostRequestDto) {

        EstimatedCostResponse response =
                businessModelService.calculateEstimatedCost(estimatedCostRequestDto);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/confirmTourByTourist/{bookingId}")
    public ResponseEntity<ActionResponse> confirmTourByTourist(
            @PathVariable Long bookingId
    ) {
        ActionResponse response = bookingService.confirmBookingByTourist(bookingId);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancelTourByTourist/{bookingId}")
    public ResponseEntity<ActionResponse> cancelTourByTourist(
            @PathVariable Long bookingId
    ) {
        ActionResponse response = bookingService.cancelTourByTourist(bookingId);

        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("get_bookings_by_driver/{driverId}")
    public ResponseEntity<List<BookingSystemResponseDto>> getBookingsByDriver(@PathVariable Long driverId) {
        List<BookingSystemResponseDto> bookings = bookingService.getBookingsByDriverId(driverId);
        return ResponseEntity.ok(bookings);
    }


}
