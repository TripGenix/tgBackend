package com.tripgenix.AuthService.config;

import com.tripgenix.AuthService.model.Tourist;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final String SECRET_KEY = Base64.getEncoder()
            .encodeToString("my-super-secure-secret-key-1234567890".getBytes());
    private final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 hours

    public String generateToken(Tourist user) {
        return Jwts.builder()
                .setSubject(user.getEmail())               // user identifier
                .claim("userId", user.getTouristId())      // custom claim
                .claim("firstName", user.getFirstName())   // custom claim
                .claim("lastName", user.getLastName())     // custom claim
                .claim("role", "TOURIST")                  // custom claim
                .claim("profileImageUrl", user.getProfileImageUrl())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
//test