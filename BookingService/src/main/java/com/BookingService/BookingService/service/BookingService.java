package com.BookingService.BookingService.service;

import com.BookingService.BookingService.dto.BookingRequestDto;
import com.BookingService.BookingService.dto.BookingResponseDto;
import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Route;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.RouteRepository;
import com.BookingService.BookingService.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {


        // Save Trip
        Trip trip = new Trip();
        trip.setStartDateTime(dto.getTripDetails().getStartDate().atStartOfDay());
        trip.setEndDateTime(dto.getTripDetails().getEndDate().atStartOfDay());
        trip.setEstimatedCost(BigDecimal.valueOf(dto.getRouteDetails().getBookingPrice()));
        trip.setDistance(dto.getRouteDetails().getDistance());
        trip.setDuration(dto.getRouteDetails().getDuration());
        trip.setStartLocation(dto.getTripDetails().getStartLocation());
        trip.setEndLocation(dto.getTripDetails().getEndLocation());
        trip.setStatus("PENDING");

        Trip savedTrip = tripRepository.save(trip);

        // Save Routes
        for (String destination : dto.getTripDetails().getDestinations()) {
            Route route = new Route();
            route.setTripId(savedTrip.getTripId());
            route.setWayPoint(destination);
            routeRepository.save(route);
        }

        // ⃣ Save Booking

        Booking booking = new Booking();

        // Relations
        booking.setTripId(savedTrip.getTripId());
        booking.setTouristId(dto.getUser().getUserId());
        booking.setPackageId(dto.getMetadata().getPackageId());

        // Booker info
        booking.setBookerName(dto.getBookingDetails().getNameOfBooker());
        booking.setBookerEmail(dto.getBookingDetails().getBookerEmail());
        booking.setBookerPhone(dto.getBookingDetails().getBookerPhone());
        booking.setPassportNumber(dto.getBookingDetails().getPassportNumber());

        // Passengers
        booking.setAdults(dto.getBookingDetails().getPassengers().getAdults());
        booking.setChildren(dto.getBookingDetails().getPassengers().getChildren());
        booking.setBabies(dto.getBookingDetails().getPassengers().getBabies());

        // Flight info
        booking.setArrivalDateTime(dto.getBookingDetails().getArrivalDateTime());
        booking.setDepartureDateTime(dto.getBookingDetails().getDepartureDateTime());
        booking.setFlightNumber(dto.getBookingDetails().getFlightNumber());
        booking.setDepartureAirport(dto.getBookingDetails().getDepartureAirport());

        // Resources
        booking.setVehicleId(
                dto.getResources().getVehicle() != null
                        ? dto.getResources().getVehicle().getVehicleId().intValue()
                        : null
        );

        booking.setDriverId(
                dto.getResources().getDriver() != null
                        ? dto.getResources().getDriver().getDriverId().intValue()
                        : null
        );

        // Status & flags
        booking.setStatus("NEW");
        booking.setIsTouristConfirm(false);
        booking.setIsDriverConfirm(false);
        booking.setIsTouristCancelled(false);
        booking.setIsDriverCancelled(false);
        booking.setSendConfirmEmail(false);

        booking.setDateCreated(LocalDateTime.now());
        booking.setReferenceId("");

        Booking savedBooking = bookingRepository.save(booking);


        //  Generate Reference ID

        String referenceId = generateReferenceId(savedBooking.getBookingId());
        savedBooking.setReferenceId(referenceId);

        bookingRepository.save(savedBooking);


        //Response

        BookingResponseDto response = new BookingResponseDto();
        response.setReferenceId(referenceId);
        response.setBookingId(savedBooking.getBookingId());
        response.setTripId(savedTrip.getTripId());
        response.setTouristId(savedBooking.getTouristId());
        response.setStatus(savedBooking.getStatus());
        response.setCreatedAt(savedBooking.getDateCreated());

        return response;
    }


    // Reference ID Generator
    private String generateReferenceId(Long bookingId) {
        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd

        return "TG-CP-" + date + "-" + bookingId;
    }
}
