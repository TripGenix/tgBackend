package com.BookingService.BookingService.dto.systemReponse;

import com.BookingService.BookingService.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingSystemResponseById {
    private String referenceId;
    private UserDto user;
    private BookingDetailsDto bookingDetails;
    private TripDetailsDto tripDetails;
    private RouteDetailsDto routeDetails;
    private ResourcesDto resources;
    private MetadataDto metadata;

}
