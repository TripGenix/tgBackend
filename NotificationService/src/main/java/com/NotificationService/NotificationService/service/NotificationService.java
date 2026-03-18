package com.NotificationService.NotificationService.service;

import com.NotificationService.NotificationService.dto.NotificationDto;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private FCMService fcmService;

    @Value("${driver.service.url:http://localhost:8092}")
    private String driverServiceUrl;

    private WebClient driverServiceClient;

    public FCMService getFcmService() {
        return fcmService;
    }

    /**
     * Send real-time notification to driver via WebSocket
     */
    public void sendWebSocketNotification(Integer driverId, NotificationDto notification) {
        try {
            messagingTemplate.convertAndSend("/topic/driver/" + driverId, notification);
            System.out.println("✅ WebSocket notification sent to driver: " + driverId);
        } catch (Exception e) {
            System.err.println("❌ WebSocket notification failed: " + e.getMessage());
        }
    }

    /**
     * Send push notification to driver's Flutter app via FCM
     */
    public void sendFCMPushNotification(Integer driverId, NotificationDto notification) {
        try {
            // Fetch driver's FCM token from DriverManagement service
            String fcmToken = getDriverFCMToken(driverId);
            
            if (fcmToken == null || fcmToken.isEmpty()) {
                System.out.println("⚠️ No FCM token found for driver: " + driverId);
                return;
            }

            // CRITICAL: Prepare data payload for Flutter app
            // Data payload is MANDATORY for onMessage to fire in background
            // Always create data map (never null/empty)
            Map<String, String> data = new HashMap<>();
            data.put("type", notification.getType() != null ? notification.getType() : "");
            data.put("bookingId", notification.getBookingId() != null ? String.valueOf(notification.getBookingId()) : "");
            data.put("driverId", String.valueOf(notification.getDriverId()));
            
            // Add additional data from notification DTO
            if (notification.getData() != null) {
                notification.getData().forEach((key, value) -> 
                    data.put(key, value != null ? value.toString() : "")
                );
            }

            // Send FCM notification
            // FCMService will ensure data payload is always included (mandatory)
            // Notification payload will be added if title/body are provided (optional)
            fcmService.sendNotification(fcmToken, notification.getTitle(), notification.getMessage(), data);
            System.out.println("✅ FCM push notification sent to driver: " + driverId);
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM push notification failed: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error sending FCM notification: " + e.getMessage());
        }
    }

    /**
     * Send notification to driver using both WebSocket and FCM
     */
    public void notifyDriver(Integer driverId, NotificationDto notification) {
        // Send via WebSocket (for web apps)
        sendWebSocketNotification(driverId, notification);
        
        // Send via FCM (for Flutter mobile app)
        sendFCMPushNotification(driverId, notification);
    }

    /**
     * Fetch driver's FCM token from DriverManagement service
     */
    private String getDriverFCMToken(Integer driverId) {
        try {
            if (driverServiceClient == null) {
                // Initialize WebClient if not already done
                driverServiceClient = WebClient.builder()
                        .baseUrl(driverServiceUrl)
                        .build();
            }
            
            // Call DriverManagement service to get driver's FCM token
            String fcmToken = driverServiceClient.get()
                    .uri("/driveController/api/v1/" + driverId + "/fcm-token")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            return fcmToken;
        } catch (Exception e) {
            System.err.println("❌ Error fetching driver FCM token: " + e.getMessage());
        }
        return null;
    }
}

