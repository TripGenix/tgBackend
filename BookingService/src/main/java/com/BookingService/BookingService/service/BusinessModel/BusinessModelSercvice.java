package com.BookingService.BookingService.service.BusinessModel;

import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostRequestDto;

import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostResponse;

import com.BookingService.BookingService.dto.BusinessModel.VehicleReciveDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

        double D = dto.getDistance();
        int N = calculateTripDays(D);

        double Cd = vehicle.getCostPerKm().doubleValue();
        double An = vehicle.getDriverSalaryPerDay().doubleValue();
//        double On = vehicle.getOvernightCharge().doubleValue();
        double On = 3000;

        // Distance-based cost
        double Td = D * Cd;

        // Day-based cost
        double Tday = (N * An) + ((N - 1) * On);

        // Base trip cost
        double T = Td + Tday;

        // Commission
        double platformFee = T * 0.10;
        double touristPays = T + platformFee;
        double driverReceives = T - platformFee;
        double tripGeixEarns = T * 0.20;

        return new EstimatedCostResponse(
                T,
                touristPays,
                driverReceives,
                tripGeixEarns,
                N
        );
    }

    public int calculateTripDays(double distanceKm) {

        final int MAX_KM_PER_DAY = 500;

        if (distanceKm <= 0) {
            throw new IllegalArgumentException("Distance must be greater than 0");
        }

        return (int) Math.ceil(distanceKm / MAX_KM_PER_DAY);
    }




}
