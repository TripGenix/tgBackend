package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository  extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(String status);

    @Query(value = "SELECT * FROM bookings WHERE status = :status OR is_tourist_confirm = :confirm",
            nativeQuery = true)
    List<Booking> findConfirmedOrTouristConfirmed(
            @Param("status") String status,
            @Param("confirm") boolean confirm
    );


    List<Booking> findByIsDriverConfirm(boolean b);

    Optional<Object> findByReferenceId(String orderId);

    List<Booking> findByTouristId(Long touristId);

}
