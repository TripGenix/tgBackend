package com.driverManagement.DriverManagement.services;

import com.BookingService.BookingService.dto.EmailDetailsDto;
import com.driverManagement.DriverManagement.Config.OtpGenerator;
import com.driverManagement.DriverManagement.Dto.ConfirmBookingEmailRequest;
import com.driverManagement.DriverManagement.Dto.TourStatusUpdateDto;
import com.driverManagement.DriverManagement.models.Booking;
import com.driverManagement.DriverManagement.models.Trip;
import com.driverManagement.DriverManagement.repository.TourRepository;
import com.driverManagement.DriverManagement.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TourService {

    @Autowired
    TourRepository tourRepo;

    @Autowired
    TripRepository tripRepo;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    @Autowired
    NotificationService notificationService;

    @Autowired
    OtpGenerator otpGenerator;

    private final WebClient webClient;

    public TourService(WebClient webClient) {
        this.webClient = webClient;

    }

    public List<Booking> getPendingApprovedBookings(int driverId) {
        return tourRepo.findByDriverIdAndIsDriverConfirmAndIsDriverCancelled(driverId,false,false);
    }

    public List<Booking> getcancledBookings(int driverId) {
        return tourRepo.findByDriverIdAndIsDriverConfirmAndIsDriverCancelled(driverId,false,true);
    }

    public TourStatusUpdateDto confirmTour(int tourId) {

        Booking tour = tourRepo.findById(tourId)
                .orElseThrow(() ->
                        new RuntimeException("Tour not found with id: " + tourId)
                );

        if (tour.getIsDriverConfirm()) {
            return new TourStatusUpdateDto(tourId, "ALREADY_CONFIRMED");
        }

        if (tour.getIsDriverCancelled()) {
            return new TourStatusUpdateDto(tourId, "CANNOT_CONFIRM_CANCELLED");
        }

        tour.setIsDriverConfirm(true);
        tour.setStatus("DRIVER_CONFIRMED");
        tour.setDriverConfirmedAt(LocalDateTime.now());

        try{
            tourRepo.save(tour);
            webClient.post()
                    .uri(
                            "http://localhost:8087/bookingservice/api/v1/send_confirm_booking_email/{id}",
                            tour.getBookingId()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .doOnSuccess(res ->
                            System.out.println("✅ Email trigger sent to Booking Service")
                    )
                    .doOnError(err ->
                            System.err.println("❌ Failed to trigger email: " + err.getMessage())
                    )
                    .subscribe();
        }catch (Exception e){
            return new TourStatusUpdateDto(tourId, "ERROR");
        }

        messagingTemplate.convertAndSend(
                "/topic/driver-confirm",
                new TourStatusUpdateDto(tourId, "CONFIRMED")
        );
        return new TourStatusUpdateDto(tourId, "CONFIRMED");
    }

    public TourStatusUpdateDto cancelTour(int tourId) {

        Booking tour = tourRepo.findById(tourId)
                .orElseThrow(() ->
                        new RuntimeException("Tour not found with id: " + tourId)
                );

        if (tour.getIsDriverCancelled()) {
            return new TourStatusUpdateDto(tourId, "ALREADY_CANCELLED");
        }

        if (tour.getIsDriverConfirm()) {
            return new TourStatusUpdateDto(tourId, "CANNOT_CANCEL_CONFIRMED");
        }

        tour.setIsDriverCancelled(true);
        tour.setDriverCancelledAt(LocalDateTime.now());

        tourRepo.save(tour);

        return new TourStatusUpdateDto(tourId, "CANCELLED");
    }


    public void requestTourStart(int tourId) {

        Booking tour = tourRepo.findById(tourId)
                .orElseThrow(() ->
                        new RuntimeException("Tour not found with id: " + tourId)
                );

        Trip trip = tripRepo.findByTripId(
                Math.toIntExact(tour.getTripId())
        );

        String otp = otpGenerator.generateOtp();

        trip.setStartOtp(otp);
        trip.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        trip.setOtpVerified(false);

        tripRepo.save(trip);

        notificationService.sendOtp(
                tour.getTouristId(),
                otp
        );
    }


    public void verifyOtpAndStartTour(int tourId, String enteredOtp) {

        Booking tour = tourRepo.findById(tourId)
                .orElseThrow(() ->
                        new RuntimeException("Tour not found")
                );

        Trip trip = tripRepo.findByTripId(
                Math.toIntExact(tour.getTripId())
        );

        if (trip.getOtpExpiry() == null ||
                trip.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        if (!trip.getStartOtp().equals(enteredOtp)) {
            throw new RuntimeException("Invalid OTP");
        }

        trip.setTourStart(true);
        tour.setStatus("STARTED");
        trip.setTourStartDateTime(LocalDateTime.now());
        trip.setOtpVerified(true);

        // clear OTP
        trip.setStartOtp(null);
        trip.setOtpExpiry(null);

        tripRepo.save(trip);
    }

    public void finishTour(int tourId) {
        Booking tour = tourRepo.findById(tourId)
                .orElseThrow(() ->
                        new RuntimeException("Tour not found")
                );

        Trip trip = tripRepo.findByTripId(
                Math.toIntExact(tour.getTripId())
        );

        trip.setTourEnd(true);
        tour.setStatus("FINISHED");
        trip.setTourEndDateTime(LocalDateTime.now());
        trip.setOtpVerified(true);

        tripRepo.save(trip);
    }
}
