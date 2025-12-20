package com.tripgenix.AuthService.controller;

import com.tripgenix.AuthService.config.JwtTokenProvider;
import com.tripgenix.AuthService.dto.LoginRequestDto;
import com.tripgenix.AuthService.dto.LoginResposnseDto;
import com.tripgenix.AuthService.dto.TouristDto;
import com.tripgenix.AuthService.dto.TouristResponseDto;
import com.tripgenix.AuthService.model.Tourist;
import com.tripgenix.AuthService.services.TouristService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("api/v1/tourists")
public class TouristController {

    private final TouristService touristService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public TouristController(
            TouristService touristService,
            AuthenticationManager authenticationManager,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.touristService = touristService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<TouristResponseDto> register(@RequestBody TouristDto dto){
        TouristResponseDto response = touristService.saveTourist(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public LoginResposnseDto login(@RequestBody LoginRequestDto request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Tourist user = touristService.findByEmail(request.getEmail());

        String token = jwtTokenProvider.generateToken(user);

        return new LoginResposnseDto(token);
    }
}
