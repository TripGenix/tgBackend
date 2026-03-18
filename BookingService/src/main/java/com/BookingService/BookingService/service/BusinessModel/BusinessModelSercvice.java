package com.BookingService.BookingService.service.BusinessModel;

import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostRequestDto;
import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostResponse;
import com.BookingService.BookingService.dto.BusinessModel.VehicleReciveDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BusinessModelSercvice {

    private final WebClient webClient;

    public BusinessModelSercvice(WebClient webClient) {
        this.webClient = webClient;
    }

    public EstimatedCostResponse calculateEstimatedCost(
            EstimatedCostRequestDto dto) {

        VehicleReciveDto vehicle = webClient.get()
                .uri("http://localhost:8085/vehicleController/api/v1/detailsOfVehicle/{id}",
                        dto.getVehicleId())
                .retrieve()
                .bodyToMono(VehicleReciveDto.class)
                .block();

        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }

        // ================= BASIC DATA =================

        BigDecimal distance = BigDecimal.valueOf(dto.getDistance());
        int tripDays = calculateTripDays(dto.getDistance());
        int nights = Math.max(tripDays - 1, 0);

        // ================= VEHICLE SALARY =================

        BigDecimal vehicleSalary = tripSalaryForVehicle(vehicle, distance);

        // ================= DRIVER SALARY =================

        BigDecimal driverSalary = tripSalaryForDriver(vehicle, tripDays, nights);

        // ================= BASE COST =================

        BigDecimal baseCost = vehicleSalary.add(driverSalary);

        // ================= PLATFORM COMMISSION (10%) =================

        BigDecimal commissionRate = new BigDecimal("0.10");

        BigDecimal platformFee = baseCost
                .multiply(commissionRate)
                .setScale(2, RoundingMode.HALF_UP);

        // ================= TOURIST TOTAL =================

        BigDecimal touristPays = baseCost
                .add(platformFee)
                .setScale(2, RoundingMode.HALF_UP);

        // ================= FINAL DISTRIBUTION =================

        BigDecimal driverReceives = driverSalary.setScale(2, RoundingMode.HALF_UP);
        BigDecimal vehicleReceives = vehicleSalary.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tripGenixEarns = platformFee;

        return new EstimatedCostResponse(
                baseCost.setScale(2, RoundingMode.HALF_UP),
                touristPays.setScale(2, RoundingMode.HALF_UP),
                driverReceives.setScale(2, RoundingMode.HALF_UP),
                vehicleReceives.setScale(2, RoundingMode.HALF_UP),
                tripGenixEarns.setScale(2, RoundingMode.HALF_UP),
                tripDays
        );

    }

    // ================= CALCULATE TRIP DAYS =================

    public int calculateTripDays(double distanceKm) {

        final int MAX_KM_PER_DAY = 500;

        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }

        return (int) Math.ceil(distanceKm / MAX_KM_PER_DAY);
    }

    // ================= DRIVER SALARY =================

    public BigDecimal tripSalaryForDriver(
            VehicleReciveDto dto,
            int days,
            int nights) {

        BigDecimal overnightCharge = new BigDecimal("3000");
        BigDecimal salaryPerDay = dto.getDriverSalaryPerDay();

        BigDecimal salaryForDays = salaryPerDay.multiply(BigDecimal.valueOf(days));
        BigDecimal salaryForNights = overnightCharge.multiply(BigDecimal.valueOf(nights));

        return salaryForDays.add(salaryForNights);
    }

    // ================= VEHICLE SALARY =================

    public BigDecimal tripSalaryForVehicle(
            VehicleReciveDto dto,
            BigDecimal distanceKm) {

        if (distanceKm.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }

        BigDecimal costPerKm = dto.getCostPerKm();

        return costPerKm.multiply(distanceKm);
    }
}
