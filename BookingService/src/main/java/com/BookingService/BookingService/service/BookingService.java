package com.BookingService.BookingService.service;

import com.BookingService.BookingService.dto.*;
import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseById;
import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseDto;
import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Route;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.RouteRepository;
import com.BookingService.BookingService.repository.TripRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final WebClient webClient;

    public BookingService(WebClient webClient) {
        this.webClient = webClient;
    }


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

        List<String> routeList = dto.getTripDetails().getDestinations();

        BookingResponseDto response = new BookingResponseDto();

        response.setBookingId(savedBooking.getBookingId());
        response.setTripId(savedTrip.getTripId());
        response.setCustomerName(savedBooking.getBookerName());
        response.setRoute(routeList);
        response.setStartDate(savedTrip.getStartDateTime());
        response.setEndDate(savedTrip.getEndDateTime());
        response.setTouristId(savedBooking.getTouristId());
        response.setStatus(savedBooking.getStatus());
        response.setCreatedAt(savedBooking.getDateCreated());
        response.setReferenceId(savedBooking.getReferenceId());

        return response;
    }


    // Reference ID Generator
    private String generateReferenceId(Long bookingId) {
        String date = LocalDate.now()
                .format(DateTimeFormatter.BASIC_ISO_DATE); // yyyyMMdd

        return "TG-CP-" + date + "-" + bookingId;
    }


    public List<BookingSystemResponseDto> getNewBookings() {

        // Fetch NEW bookings
        List<Booking> bookings = bookingRepository.findByStatus("NEW");

        //Map entity list → DTO list
        List<BookingSystemResponseDto> dtoList =
                modelMapper.map(
                        bookings,
                        new TypeToken<List<BookingSystemResponseDto>>() {}.getType()
                );

        //  Enrich DTOs with derived & missing fields
        for (int i = 0; i < bookings.size(); i++) {

            Booking booking = bookings.get(i);
            BookingSystemResponseDto dto = dtoList.get(i);

            // FIX: manually map createdAt
            dto.setCreatedAt(booking.getDateCreated());

            //Route
            dto.setRoute(
                    routeRepository.findWayPointsByTripId(booking.getTripId())
            );

            // Trip dates
            tripRepository.findById(booking.getTripId()).ifPresent(trip -> {
                dto.setStartDate(trip.getStartDateTime());
                dto.setEndDate(trip.getEndDateTime());
            });
        }

        return dtoList;
    }


    public List<BookingSystemResponseDto> getAllBookings() {

        //Fetch all bookings
        List<Booking> bookings = bookingRepository.findAll();

        //  Map entity list → DTO list
        List<BookingSystemResponseDto> dtoList =
                modelMapper.map(
                        bookings,
                        new TypeToken<List<BookingSystemResponseDto>>() {}.getType()
                );

        // Enrich DTOs with missing / derived fields
        for (int i = 0; i < bookings.size(); i++) {

            Booking booking = bookings.get(i);
            BookingSystemResponseDto dto = dtoList.get(i);

            //  manually map createdAt (name mismatch)
            dto.setCreatedAt(booking.getDateCreated());

            // Route (derived)
            dto.setRoute(
                    routeRepository.findWayPointsByTripId(booking.getTripId())
            );

            //  Trip dates (derived)
            tripRepository.findById(booking.getTripId()).ifPresent(trip -> {
                dto.setStartDate(trip.getStartDateTime());
                dto.setEndDate(trip.getEndDateTime());
            });
        }

        return dtoList;
    }


    public BookingSystemResponseById getBookingById(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        BookingSystemResponseById response = new BookingSystemResponseById();

        // Reference
        response.setReferenceId(booking.getReferenceId());

        // User
        response.setUser(
                new UserDto(
                        booking.getTouristId(),
                        "TOURIST"
                )
        );

        // Booking details
        response.setBookingDetails(
                new BookingDetailsDto(
                        booking.getBookerName(),
                        booking.getPassportNumber(),
                        booking.getBookerEmail(),
                        booking.getBookerPhone(),
                        booking.getArrivalDateTime(),
                        booking.getDepartureDateTime(),
                        booking.getFlightNumber(),
                        booking.getDepartureAirport(),
                        null // passengers (map if needed)
                )
        );

        // Trip details
        response.setTripDetails(
                new TripDetailsDto(
                        trip.getStartLocation(),
                        trip.getEndLocation(),
                        trip.getStartDateTime().toLocalDate(),
                        trip.getEndDateTime().toLocalDate(),
                        booking.getVehicleId() != null,
                        routeRepository.findWayPointsByTripId(trip.getTripId())
                )
        );

        // Route details
        response.setRouteDetails(
                new RouteDetailsDto(
                        trip.getDistance(),
                        trip.getDuration(),
                        null,
                        null,
                        trip.getEstimatedCost().doubleValue()
                )
        );

        // Resources
        response.setResources(
                new ResourcesDto(
                        null, // vehicle
                        null, // driver
                        null  // guide
                )
        );

        // Metadata
        response.setMetadata(
                new MetadataDto(
                        booking.getDateCreated(),
                        "SYSTEM",
                        booking.getPackageId()
                )
        );

        return response;
    }

    public void sendEmail(Long bookingId, EmailDetailsDto emailDetailsDto) {

        webClient.post()
                .uri("http://localhost:8088/email/api/v1/send")
                .bodyValue(emailDetailsDto)
                .retrieve()
                .bodyToMono(String.class)
                .doOnSuccess(response -> {
                    System.out.println("✅ Email sent: " + response);

                    updateEmailStatus(bookingId,true);
                })
                .doOnError(error -> {
                    System.err.println("❌ Email failed: " + error.getMessage());
                    updateEmailStatus(bookingId,false);

                })
                .subscribe();


    }

    @Transactional
    public void updateEmailStatus(Long bookingId, boolean status) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setSendConfirmEmail(status);

        bookingRepository.save(booking);
    }


}
