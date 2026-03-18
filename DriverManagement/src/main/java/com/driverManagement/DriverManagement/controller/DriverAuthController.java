package com.driverManagement.DriverManagement.controller;

import com.driverManagement.DriverManagement.Dto.*;
import com.driverManagement.DriverManagement.services.DriverAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/driveController/api/v1/auth")
public class DriverAuthController {

    @Autowired
    private DriverAuthService driverAuthService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DriverRegisterDto dto) {
        try {
            DriverRegisterResponseDto response = driverAuthService.register(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody OtpVerificationDto dto) {
        try {
            DriverLoginResponseDto response = driverAuthService.verifyOtpAndLogin(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DriverLoginDto dto) {
        try {
            DriverLoginResponseDto response = driverAuthService.login(dto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam String email) {
        try {
            String message = driverAuthService.resendOtp(email);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

