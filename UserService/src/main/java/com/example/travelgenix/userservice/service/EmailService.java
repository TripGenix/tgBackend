package com.example.travelgenix.userservice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendInvite(String email, String password) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Admin Invitation");

        msg.setText(
                "You are invited as Admin.\n\n" +
                        "Login Email: " + email + "\n" +
                        "Temporary Password: " + password + "\n\n" +
                        "Please change your password after login."
        );

        mailSender.send(msg);
    }
}
