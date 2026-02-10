package com.NotificationService.NotificationService.controller;

import com.NotificationService.NotificationService.dto.FCMNotificationRequest;
import com.NotificationService.NotificationService.dto.NotificationDto;
import com.NotificationService.NotificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification/api/v1")
@CrossOrigin("*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Notify a specific driver about a new ride request
     * This endpoint is called by BookingService when a booking is created
     */
    @PostMapping("/notify-driver/{driverId}")
    public ResponseEntity<String> notifyDriver(
            @PathVariable Integer driverId,
            @RequestBody NotificationDto notification) {
        try {
            notification.setDriverId(driverId);
            notificationService.notifyDriver(driverId, notification);
            return ResponseEntity.ok("Notification sent successfully to driver: " + driverId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send notification: " + e.getMessage());
        }
    }

    /**
     * Send FCM push notification directly (for testing)
     */
    @PostMapping("/send-fcm")
    public ResponseEntity<String> sendFCMNotification(@RequestBody FCMNotificationRequest request) {
        try {
            String response = notificationService.getFcmService().sendNotification(request);
            return ResponseEntity.ok("FCM notification sent: " + response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send FCM notification: " + e.getMessage());
        }
    }

    /**
     * Test WebSocket notification
     */
    @PostMapping("/test-websocket/{driverId}")
    public ResponseEntity<String> testWebSocket(@PathVariable Integer driverId) {
        NotificationDto notification = new NotificationDto();
        notification.setType("TEST");
        notification.setTitle("Test Notification");
        notification.setMessage("This is a test notification");
        notification.setDriverId(driverId);
        
        notificationService.sendWebSocketNotification(driverId, notification);
        return ResponseEntity.ok("Test WebSocket notification sent");
    }
}

