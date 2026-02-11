package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.dto.systemReponse.VehiclePaymentsResponseDto;
import com.BookingService.BookingService.model.VehiclePayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VehiclePaymentsRepository extends JpaRepository<VehiclePayments, Long> {
    Optional<Object> findByBookingId(Long bookingId);

    @Query("""
        SELECT new com.BookingService.BookingService.dto.systemReponse.VehiclePaymentsResponseDto(
            vp.id,
            vp.paymentId,
            vp.bookingId,
            b.referenceId,
            v.vehicleName,
            vp.amount,
            vp.status,
            vp.paymentDateTime
        )
        FROM VehiclePayments vp
        JOIN Booking b ON vp.bookingId = b.bookingId
        JOIN Vehicle v ON b.vehicleId = v.vehicleId
        ORDER BY vp.paymentDateTime DESC
    """)
    List<VehiclePaymentsResponseDto> getVehiclePaymentsWithDetails();
}
