package com.BookingService.BookingService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Value("${notification.service.url:http://13.218.211.254:8089}")
    private String notificationServiceUrl;
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
    
    @Bean(name = "notificationWebClient")
    public WebClient notificationWebClient() {
        return WebClient.builder()
                .baseUrl(notificationServiceUrl)
                .build();
    }
}
