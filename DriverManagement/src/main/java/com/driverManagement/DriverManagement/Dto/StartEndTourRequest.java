package com.driverManagement.DriverManagement.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartEndTourRequest {
    int driverId;
    String status;
}
