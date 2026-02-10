package com.driverManagement.DriverManagement.services;

import com.driverManagement.DriverManagement.Dto.TourStatusUpdateDto;
import com.driverManagement.DriverManagement.models.Booking;
import com.driverManagement.DriverManagement.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TourService {

    @Autowired
    TourRepository tourRepo;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

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
        tour.setDriverConfirmedAt(LocalDateTime.now());

        try{
            tourRepo.save(tour);
            webClient.post()
                    .uri(
                            "http://localhost:8084/bookingservice/api/v1/send_confirm_booking_email/{id}",
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
                "/topic/tour-updates",
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

}
