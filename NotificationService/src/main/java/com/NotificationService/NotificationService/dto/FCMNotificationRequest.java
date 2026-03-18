package com.NotificationService.NotificationService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * FCM Notification Request DTO
 * 
 * CRITICAL: Data payload is MANDATORY for onMessage to fire in background Flutter apps.
 * If data is not provided, it will be created from title/body fields.
 * 
 * Notification payload (title/body) is optional - used for system overlay.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FCMNotificationRequest {
    private String fcmToken;
    private String title; // Optional - for notification payload
    private String body;  // Optional - for notification payload
    private Map<String, String> data; // MANDATORY - will be created from title/body if missing
}

