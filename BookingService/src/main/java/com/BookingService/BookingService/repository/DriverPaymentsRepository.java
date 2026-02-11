package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.dto.systemReponse.DriverPaymentsResponseDto;
import com.BookingService.BookingService.model.DriverPayments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DriverPaymentsRepository
        extends JpaRepository<DriverPayments, Long> {

    Optional<DriverPayments> findByBookingId(Long bookingId);

    @Query("""
    SELECT new com.BookingService.BookingService.dto.systemReponse.DriverPaymentsResponseDto(
        dp.id,
        dp.paymentId,
        dp.bookingId,
        b.referenceId,
        CONCAT(d.firstName, ' ', d.lastName),
        dp.amount,
        dp.status,
        dp.paymentDateTime
    )
    FROM DriverPayments dp
    JOIN Booking b ON dp.bookingId = b.bookingId
    JOIN Driver d ON b.driverId = d.driverId
    ORDER BY dp.paymentDateTime DESC
""")
    List<DriverPaymentsResponseDto> getDriverPaymentsWithDetails();

}
