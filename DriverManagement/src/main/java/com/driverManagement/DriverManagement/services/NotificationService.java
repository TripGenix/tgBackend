package com.driverManagement.DriverManagement.services;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void sendOtp(Long touristId, String otp) {
        // TODO: integrate real email/SMS service
        System.out.println("Sending OTP " + otp + " to " + touristId);
    }
}
