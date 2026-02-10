package com.driverManagement.DriverManagement.config;

import com.driverManagement.DriverManagement.models.Driver;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import io.jsonwebtoken.Claims;

@Component
public class JwtTokenProvider {

    private static final String SECRET_STRING = "driver-super-secure-secret-key-1234567890-driver-super-secure-secret-key-1234567890";
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(SECRET_STRING.getBytes());
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(Driver driver) {
        return Jwts.builder()
                .subject(driver.getEmail())               // user identifier
                .claim("driverId", driver.getDriverId())     // custom claim
                .claim("firstName", driver.getFirstName())   // custom claim
                .claim("lastName", driver.getLastName())     // custom claim
                .claim("role", "DRIVER")                     // custom claim
                .claim("emailVerified", driver.getEmailVerified())
                .claim("isApproved", driver.getIsApproved())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SECRET_KEY)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

