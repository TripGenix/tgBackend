package com.NotificationService.NotificationService.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.NotificationService.NotificationService.dto.FCMNotificationRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FCMService {

    /**
     * Send push notification to Flutter app using FCM token
     * 
     * CRITICAL: Data payload is MANDATORY for onMessage to fire in background.
     * Notification payload is optional (for system overlay).
     * 
     * Message structure (with notification):
     * {
     *   "token": "FCM_TOKEN",
     *   "notification": {
     *     "title": "...",
     *     "body": "..."
     *   },
     *   "data": {
     *     "type": "...",
     *     "bookingId": "...",
     *     ...
     *   }
     * }
     * 
     * Message structure (data only):
     * {
     *   "token": "FCM_TOKEN",
     *   "data": {
     *     "type": "...",
     *     "bookingId": "...",
     *     "title": "...",
     *     "body": "...",
     *     ...
     *   }
     * }
     */
    public String sendNotification(FCMNotificationRequest request) throws FirebaseMessagingException {
        try {
            Message.Builder messageBuilder = Message.builder()
                    .setToken(request.getFcmToken());

            // CRITICAL: Always create data payload (mandatory for background onMessage)
            Map<String, String> dataPayload = new HashMap<>();
            if (request.getData() != null && !request.getData().isEmpty()) {
                // Use provided data
                dataPayload.putAll(request.getData());
            } else {
                // Create data from notification fields if missing
                dataPayload.put("type", "NOTIFICATION");
                if (request.getTitle() != null) {
                    dataPayload.put("title", request.getTitle());
                }
                if (request.getBody() != null) {
                    dataPayload.put("body", request.getBody());
                }
            }
            // Always include data payload (mandatory)
            messageBuilder.putAllData(dataPayload);

            // Notification payload is optional (for system overlay)
            if (request.getTitle() != null && request.getBody() != null) {
                Notification notification = Notification.builder()
                        .setTitle(request.getTitle())
                        .setBody(request.getBody())
                        .build();
                messageBuilder.setNotification(notification);
            }

            Message message = messageBuilder.build();

            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ FCM notification sent successfully: " + response);
            return response;
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ FCM notification failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Send notification with custom data map
     */
    public String sendNotification(String fcmToken, String title, String body, Map<String, String> data) 
            throws FirebaseMessagingException {
        FCMNotificationRequest request = new FCMNotificationRequest(fcmToken, title, body, data);
        return sendNotification(request);
    }
}

