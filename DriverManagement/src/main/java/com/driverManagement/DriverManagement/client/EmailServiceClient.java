package com.driverManagement.DriverManagement.client;

import com.driverManagement.DriverManagement.Dto.EmailDetailsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;

@Component
public class EmailServiceClient {

    private final WebClient webClient;
    
    @Value("${email.service.url:http://localhost:8088}")
    private String emailServiceUrl;

    public EmailServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public CompletableFuture<String> sendOtpEmail(String recipientEmail, String otp) {
        EmailDetailsDto emailDetails = new EmailDetailsDto();
        emailDetails.setRecipient(recipientEmail);
        emailDetails.setSubject("Driver Registration - OTP Verification");
        emailDetails.setMsgBody(
            "<html><body>" +
            "<h2>Welcome to TravelGenix Driver Portal</h2>" +
            "<p>Your OTP for email verification is:</p>" +
            "<h1 style='color: #4CAF50; font-size: 32px;'>" + otp + "</h1>" +
            "<p>This OTP will expire in 10 minutes.</p>" +
            "<p>If you didn't request this, please ignore this email.</p>" +
            "<br><p>Best regards,<br>TravelGenix Team</p>" +
            "</body></html>"
        );

        return webClient.post()
                .uri(emailServiceUrl + "/email/api/v1/send")
                .bodyValue(emailDetails)
                .retrieve()
                .bodyToMono(String.class)
                .toFuture()
                .thenApply(result -> {
                    System.out.println("✅ OTP email sent to " + recipientEmail);
                    return result;
                })
                .exceptionally(error -> {
                    System.err.println("❌ Failed to send OTP email: " + error.getMessage());
                    return "Email sending failed: " + error.getMessage();
                });
    }
}

