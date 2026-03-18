package com.NotificationService.NotificationService.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount = null;

            if (firebaseConfigPath != null && !firebaseConfigPath.isEmpty()) {
                // Initialize from external file path (Production/Server)
                serviceAccount = new FileInputStream(firebaseConfigPath);
            } else {
                // Initialize from Classpath (Development/Local)
                ClassPathResource resource = new ClassPathResource("firebase-service-account.json");
                if (resource.exists()) {
                    // USE getInputStream() INSTEAD OF getFile()
                    serviceAccount = resource.getInputStream();
                }
            }

            if (serviceAccount != null) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                if (FirebaseApp.getApps().isEmpty()) {
                    FirebaseApp.initializeApp(options);
                    System.out.println("✅ Firebase Application Initialized Successfully");
                }
            } else {
                System.out.println("⚠️ Firebase config not found. FCM push notifications will be disabled.");
            }

        } catch (IOException e) {
            System.err.println("❌ Error initializing Firebase: " + e.getMessage());
        }
    }
}