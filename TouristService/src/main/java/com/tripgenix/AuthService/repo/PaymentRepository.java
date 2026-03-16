package com.tripgenix.AuthService.repo;

import com.tripgenix.AuthService.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {


//    List<Payment> findByTouristId(Long touristId);
}
