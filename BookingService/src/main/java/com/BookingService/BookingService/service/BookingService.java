package com.BookingService.BookingService.service;
import com.BookingService.BookingService.EmailTemplates.EmailTemplateBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;


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
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

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

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    private final WebClient webClient;

    public BookingService(WebClient webClient) {
        this.webClient = webClient;
    }


    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto dto) {


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
        if (dto.getResources().getVehicle() != null) {
            booking.setVehicleId(dto.getResources().getVehicle().getVehicleId().intValue());
        }

        // Handle Driver (Can be null)
        if (dto.getResources().getDriver() != null) {
            booking.setDriverId(dto.getResources().getDriver().getDriverId().intValue());
        }

        // Handle Hotel (Can be null) - 🔴 NEW LOGIC
//        if (dto.getResources().getHotel() != null) {
//            booking.setHotelId(dto.getResources().getHotel().getHotelId().intValue());
//        }

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

        messagingTemplate.convertAndSend(
                "/topic/new-tour-add",
                response   //
        );

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
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trip not found"));

        BookingSystemResponseById response = new BookingSystemResponseById();

        // Reference
        response.setReferenceId(booking.getReferenceId());

        // User
        response.setUser(new UserDto(
                booking.getTouristId(),
                "TOURIST"
        ));

        // Passengers (FIXED)
        PassengerDto passengers = new PassengerDto(
                booking.getAdults(),
                booking.getChildren(),
                booking.getBabies()
        );

        // Booking details
        response.setBookingDetails(new BookingDetailsDto(
                booking.getBookerName(),
                booking.getPassportNumber(),
                booking.getBookerEmail(),
                booking.getBookerPhone(),
                booking.getArrivalDateTime(),
                booking.getDepartureDateTime(),
                booking.getFlightNumber(),
                booking.getDepartureAirport(),
                passengers
        ));

        // Trip details (NULL SAFE)
        response.setTripDetails(new TripDetailsDto(
                trip.getStartLocation(),
                trip.getEndLocation(),
                trip.getStartDateTime().toLocalDate(),
                trip.getEndDateTime().toLocalDate(),
                booking.getVehicleId() != null,
                routeRepository.findWayPointsByTripId(trip.getTripId()) != null
                        ? routeRepository.findWayPointsByTripId(trip.getTripId())
                        : List.of()
        ));

        // Route details (NULL SAFE)
        response.setRouteDetails(new RouteDetailsDto(
                trip.getDistance(),
                trip.getDuration(),
                null,
                null,
                trip.getEstimatedCost() != null
                        ? trip.getEstimatedCost().doubleValue()
                        : 0.0
        ));

        // Resources
        response.setResources(
                new ResourcesDto(
                        booking.getVehicleId() != null
                                ? new VehicleDto(
                                booking.getVehicleId().longValue(),
                                null,
                                null,
                                null
                        )
                                : null,

                        booking.getDriverId() != null
                                ? new DriverDto(
                                booking.getDriverId().longValue(),
                                null,
                                null,
                                null
                        )
                                : null,

                        null,
                        null
                )
        );



        // Metadata
        response.setMetadata(new MetadataDto(
                booking.getDateCreated(),
                "SYSTEM",
                booking.getPackageId()
        ));

        return response;
    }

    @Value("${web.app.url}")
    private String webAppUrl;

    public void sendEmail(Long bookingId) {

        BookingSystemResponseById booking = getBookingById(bookingId);

        EmailTemplateBuilder emailTemplateBuilder = new EmailTemplateBuilder();
        EmailDetailsDto emailDetails = new EmailDetailsDto();

        String paymentUrl = webAppUrl + "/payment/" + bookingId;
        String cancelUrl  = webAppUrl + "/cancel-tour?bookingId=" + bookingId;

        emailDetails.setMsgBody( emailTemplateBuilder.buildConfirmationEmail(booking,paymentUrl,cancelUrl));
        emailDetails.setRecipient(booking.getBookingDetails().getBookerEmail());
        emailDetails.setSubject("TripGenix Tour Confirmation Email");

        webClient.post()
                .uri("http://localhost:8088/email/api/v1/send")
                .bodyValue(emailDetails)
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


    @Transactional
    public ActionResponse confirmBookingByTourist(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (Boolean.TRUE.equals(booking.getIsTouristCancelled())) {
            return new ActionResponse(
                    false,
                    "This tour has already been cancelled. You cannot confirm it."
            );
        }

        if (Boolean.TRUE.equals(booking.getIsTouristConfirm())) {
            return new ActionResponse(
                    false,
                    "This tour is already confirmed."
            );
        }

        booking.setIsTouristConfirm(true);
        booking.setTouristConfirmedAt(LocalDateTime.now());
        booking.setStatus("CONFIRMED");

        bookingRepository.save(booking);

        messagingTemplate.convertAndSend(
                "/topic/confirmed-booking",
                   1

        );

        return new ActionResponse(
                true,
                "Tour confirmed successfully."
        );
    }


    @Transactional
    public ActionResponse cancelTourByTourist(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (Boolean.TRUE.equals(booking.getIsTouristConfirm())) {
            return new ActionResponse(
                    false,
                    "This tour has already been confirmed. You cannot cancel it."
            );
        }

        if (Boolean.TRUE.equals(booking.getIsTouristCancelled())) {
            return new ActionResponse(
                    false,
                    "This tour is already cancelled."
            );
        }

        booking.setIsTouristCancelled(true);
        booking.setTouristCancelledAt(LocalDateTime.now());
        booking.setStatus("CANCELLED");

        bookingRepository.save(booking);

        return new ActionResponse(
                true,
                "Tour cancelled successfully."
        );
    }


    public List<BookingSystemResponseDto> getConfirmedBookings() {

        // Fetch NEW bookings
        List<Booking> bookings = bookingRepository
                .findByStatus("CONFIRMED");


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

    public List<BookingSystemResponseDto> getDriverConfirmedBookings() {

        // Fetch NEW bookings
        List<Booking> bookings = bookingRepository
                .findByStatus("DRIVER_CONFIRMED");

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


    public List<BookingSystemResponseDto> getCancledBookings() {
        // Fetch NEW bookings
        List<Booking> bookings = bookingRepository
                .findByStatus("CANCELLED");


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

    public List<BookingSystemResponseDto> getStartedBookings() {
        // Fetch NEW bookings
        List<Booking> bookings = bookingRepository
                .findByStatus("STARTED");


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

    public List<BookingSystemResponseDto> getFinishedBookings() {

            List<Booking> bookings = bookingRepository
                    .findByStatus("FINISHED");


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

    @Transactional
    public BookingResponseDto editBooking(Long id,BookingRequestDto dto) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Booking not found"));

        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Trip not found"));

    /* =========================
       UPDATE TRIP
    ========================= */

        trip.setStartDateTime(dto.getTripDetails().getStartDate().atStartOfDay());
        trip.setEndDateTime(dto.getTripDetails().getEndDate().atStartOfDay());
        trip.setEstimatedCost(BigDecimal.valueOf(dto.getRouteDetails().getBookingPrice()));
        trip.setDistance(dto.getRouteDetails().getDistance());
        trip.setDuration(dto.getRouteDetails().getDuration());
        trip.setStartLocation(dto.getTripDetails().getStartLocation());
        trip.setEndLocation(dto.getTripDetails().getEndLocation());

        tripRepository.save(trip);

    /* =========================
       UPDATE ROUTES
       (Delete + Reinsert)
    ========================= */

        routeRepository.deleteByTripId(trip.getTripId());

        for (String destination : dto.getTripDetails().getDestinations()) {
            Route route = new Route();
            route.setTripId(trip.getTripId());
            route.setWayPoint(destination);
            routeRepository.save(route);
        }

    /* =========================
       UPDATE BOOKING DETAILS
    ========================= */

        booking.setBookerName(dto.getBookingDetails().getNameOfBooker());
        booking.setBookerEmail(dto.getBookingDetails().getBookerEmail());
        booking.setBookerPhone(dto.getBookingDetails().getBookerPhone());
        booking.setPassportNumber(dto.getBookingDetails().getPassportNumber());

        booking.setAdults(dto.getBookingDetails().getPassengers().getAdults());
        booking.setChildren(dto.getBookingDetails().getPassengers().getChildren());
        booking.setBabies(dto.getBookingDetails().getPassengers().getBabies());

        booking.setArrivalDateTime(dto.getBookingDetails().getArrivalDateTime());
        booking.setDepartureDateTime(dto.getBookingDetails().getDepartureDateTime());
        booking.setFlightNumber(dto.getBookingDetails().getFlightNumber());
        booking.setDepartureAirport(dto.getBookingDetails().getDepartureAirport());

    /* =========================
       UPDATE RESOURCES
    ========================= */

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

    /* =========================
       STATUS FLAGS (OPTIONAL)
    ========================= */

        // If resources change, reset confirmations
        booking.setIsDriverConfirm(false);
        booking.setSendConfirmEmail(false);

        Booking updatedBooking = bookingRepository.save(booking);

    /* =========================
       RESPONSE DTO
    ========================= */

        BookingResponseDto response = new BookingResponseDto();
        response.setBookingId(updatedBooking.getBookingId());
        response.setTripId(trip.getTripId());
        response.setCustomerName(updatedBooking.getBookerName());
        response.setRoute(dto.getTripDetails().getDestinations());
        response.setStartDate(trip.getStartDateTime());
        response.setEndDate(trip.getEndDateTime());
        response.setTouristId(updatedBooking.getTouristId());
        response.setStatus(updatedBooking.getStatus());
        response.setCreatedAt(updatedBooking.getDateCreated());
        response.setReferenceId(updatedBooking.getReferenceId());

    /* =========================
       WEBSOCKET UPDATE (OPTIONAL)
    ========================= */

//        messagingTemplate.convertAndSend(
//                "/topic/new-tour-update",
//                response
//        );

        return response;
    }

}
