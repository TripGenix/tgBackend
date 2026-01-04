package com.BookingService.BookingService.repository;

import com.BookingService.BookingService.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository  extends JpaRepository<Booking, Long> {
    List<Booking> findByStatus(String status);


}
