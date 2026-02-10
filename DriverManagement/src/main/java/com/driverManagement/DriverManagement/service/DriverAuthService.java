package com.driverManagement.DriverManagement.service;

import com.driverManagement.DriverManagement.Dto.*;
import com.driverManagement.DriverManagement.client.EmailServiceClient;
import com.driverManagement.DriverManagement.config.JwtTokenProvider;
import com.driverManagement.DriverManagement.models.Driver;
import com.driverManagement.DriverManagement.repository.DriverRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DriverAuthService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OtpService otpService;

    @Autowired
    private EmailServiceClient emailServiceClient;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Transactional
    public DriverRegisterResponseDto register(DriverRegisterDto dto) {
        // Check if email already exists
        Optional<Driver> existingDriver = driverRepository.findByEmail(dto.getEmail());
        if (existingDriver.isPresent()) {
            throw new RuntimeException("Driver with this email already exists");
        }

        // Create new driver
        Driver driver = modelMapper.map(dto, Driver.class);
        driver.setPassword(passwordEncoder.encode(dto.getPassword()));
        driver.setEmailVerified(false);
        driver.setIsDelete(false);
        driver.setIsApproved(0); // Not approved by admin yet
        driver.setStatus("PENDING");
        
        // Set default values for required fields that might be null
        if (driver.getDriverImage() == null || driver.getDriverImage().isEmpty()) {
            driver.setDriverImage(""); // Empty string as default, can be updated later
        }
        if (driver.getFcmToken() == null) {
            driver.setFcmToken(""); // Empty string as default, will be set when app registers
        }

        // Generate OTP
        String otp = otpService.generateOtp();
        driver.setOtp(otp);

        // Save driver
        driverRepository.save(driver);

        // Send OTP email asynchronously
        emailServiceClient.sendOtpEmail(driver.getEmail(), otp);

        DriverRegisterResponseDto response = new DriverRegisterResponseDto();
        response.setMessage("Registration successful. Please check your email for OTP verification.");
        response.setEmail(driver.getEmail());
        response.setEmailVerified(false);

        return response;
    }

    @Transactional
    public DriverLoginResponseDto verifyOtpAndLogin(OtpVerificationDto dto) {
        Driver driver = driverRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Verify OTP
        if (!otpService.verifyOtp(driver.getOtp(), dto.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        // Mark email as verified and clear OTP
        driver.setEmailVerified(true);
        driver.setOtp(null);
        driverRepository.save(driver);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(driver);

        // Create response
        DriverLoginResponseDto response = new DriverLoginResponseDto();
        response.setToken(token);
        response.setDriverId(driver.getDriverId());
        response.setEmail(driver.getEmail());
        response.setFirstName(driver.getFirstName());
        response.setLastName(driver.getLastName());
        response.setEmailVerified(true);
        response.setIsApproved(driver.getIsApproved());

        return response;
    }

    public DriverLoginResponseDto login(DriverLoginDto dto) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        // Get driver
        Driver driver = driverRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Check if email is verified
        if (!driver.getEmailVerified()) {
            throw new RuntimeException("Email not verified. Please verify your email first.");
        }

        // Check if driver is deleted
        if (driver.getIsDelete()) {
            throw new RuntimeException("Driver account is deleted");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(driver);

        // Create response
        DriverLoginResponseDto response = new DriverLoginResponseDto();
        response.setToken(token);
        response.setDriverId(driver.getDriverId());
        response.setEmail(driver.getEmail());
        response.setFirstName(driver.getFirstName());
        response.setLastName(driver.getLastName());
        response.setEmailVerified(driver.getEmailVerified());
        response.setIsApproved(driver.getIsApproved());

        return response;
    }

    @Transactional
    public String resendOtp(String email) {
        Driver driver = driverRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Driver not found"));

        // Generate new OTP
        String otp = otpService.generateOtp();
        driver.setOtp(otp);
        driverRepository.save(driver);

        // Send OTP email
        emailServiceClient.sendOtpEmail(driver.getEmail(), otp);

        return "OTP has been resent to your email";
    }
}

