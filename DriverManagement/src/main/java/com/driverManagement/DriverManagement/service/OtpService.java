package com.driverManagement.DriverManagement.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpService {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a 6-digit OTP
     */
    public String generateOtp() {
        int otp = 100000 + random.nextInt(900000); // Generates 6-digit number (100000-999999)
        return String.valueOf(otp);
    }

    /**
     * Verify OTP - compares stored OTP with provided OTP
     */
    public boolean verifyOtp(String storedOtp, String providedOtp) {
        if (storedOtp == null || providedOtp == null) {
            return false;
        }
        return storedOtp.equals(providedOtp);
    }
}

