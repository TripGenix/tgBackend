package com.example.travelgenix.userservice.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AdminAuthClient {

    private final RestClient adminRestClient;

    public void createCredentials(String username, String email, String password,boolean active) {

        try{
            adminRestClient.post()
                    .uri("/api/auth/admin/create-credentials")
                    .body(Map.of(
                            "username", username,
                            "email", email,
                            "password", password,
                            "active",active

                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create credentials in AdminService: " + e.getMessage());
        }
    }

    public void updateCredentials(String username, String oldEmail,String newEmail) {

        try{
            adminRestClient.put()
                    .uri("/api/auth/admin/update-credentials")
                    .body(Map.of(
                            "username", username,
                            "oldEmail", oldEmail,
                            "newEmail", newEmail


                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("Failed to update credentials in AdminService: " + e.getMessage());
        }
    }


    public void deleteCredentials(String email) {
        try {
            adminRestClient.delete()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/auth/admin/delete-credentials")
                            .queryParam("email", email)
                            .build())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete credentials in AdminService: " + e.getMessage());
        }
    }


    public void updateStatus(String email, boolean active) {
        try{
            adminRestClient.patch()
                    .uri("/api/auth/admin/status")
                    .body(Map.of("email", email, "active", active))
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            throw new RuntimeException("Failed to update status  in AdminService: " + e.getMessage());
        }
    }
}
