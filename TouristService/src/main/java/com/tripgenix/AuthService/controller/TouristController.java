package com.tripgenix.AuthService.controller;

import com.tripgenix.AuthService.config.JwtTokenProvider;
import com.tripgenix.AuthService.dto.LoginRequestDto;
import com.tripgenix.AuthService.dto.LoginResposnseDto;
import com.tripgenix.AuthService.dto.TouristDto;
import com.tripgenix.AuthService.dto.TouristResponseDto;
import com.tripgenix.AuthService.model.Tourist;
import com.tripgenix.AuthService.services.TouristService;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/test")
    public String test() {
        return "test";
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
        System.out.println(user);
        String token = jwtTokenProvider.generateToken(user);

        return new LoginResposnseDto(token, user.getTouristId());
    }

    //Get Tourist By Email
    @GetMapping("/{id}")
    public ResponseEntity<TouristResponseDto> getTourist(@PathVariable int id){
        TouristResponseDto tourist=touristService.getTouristById(id);
        return ResponseEntity.ok(tourist);
    }

    //Update Tourist
    @PutMapping("/{id}")
    public ResponseEntity<TouristResponseDto> updateTourist(
            @PathVariable int id,
            @RequestBody TouristDto dto
    ){
        TouristResponseDto updated=touristService.updateTourist(id,dto);
        return ResponseEntity.ok(updated);
    }

    //Delete Tourist
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTourist(@PathVariable int id){
        touristService.deleteTourist(id);
        return ResponseEntity.ok("Tourist Deleted Successfully");
    }

}
