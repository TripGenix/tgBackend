package com.BookingService.BookingService.service.BusinessModel;

import com.BookingService.BookingService.dto.BusinessModel.EstimatedCostRequestDto;
import com.BookingService.BookingService.dto.BusinessModel.VehicleReciveDto;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class BusinessModelSercvice {

    private final WebClient webClient;

    public BusinessModelSercvice(WebClient webClient) {
        this.webClient = webClient;
    }

    public double calculateEstimatedCost(EstimatedCostRequestDto dto) {
        double vehicleCost=0;
        Long vehicleId = dto.getVehicleId();

        VehicleReciveDto vehicle = webClient.get()
                .uri("http://localhost:8085/vehicleController/api/v1/detailsOfVehicle/{id}", vehicleId)
                .retrieve()
                .bodyToMono(VehicleReciveDto.class)
                .block();

        if (vehicle == null) {
            throw new RuntimeException("Vehicle not found");
        }

        double distance = dto.getDistance();

        vehicleCost += (vehicle.getBookingPrice().doubleValue()
                        + (distance * vehicle.getCostPerKm().doubleValue()));

        return vehicleCost;
    }
}
