package com.driverManagement.DriverManagement.Dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class DriverBlockDateDto {
    private int driverId;
    private List<LocalDate> blockedDates;
}