package com.EmailService.EmailService.service;

import com.EmailService.EmailService.Dto.EmailDetailsDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImp implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    @Async
    public CompletableFuture<String> sendSimpleMail(EmailDetailsDto details) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setSubject(details.getSubject());
            mailMessage.setText(details.getMsgBody());

            javaMailSender.send(mailMessage);

            return CompletableFuture.completedFuture("Mail sent successfully");
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    "Mail sending failed: " + e.getMessage()
            );
        }
    }

    @Override
    @Async
    public CompletableFuture<String> sendMailWithAttachment(EmailDetailsDto details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getMsgBody());

            FileSystemResource file =
                    new FileSystemResource(new File(details.getAttachment()));
            helper.addAttachment(file.getFilename(), file);

            javaMailSender.send(mimeMessage);

            return CompletableFuture.completedFuture(
                    "Mail with attachment sent successfully"
            );
        } catch (MessagingException e) {
            return CompletableFuture.completedFuture(
                    "Mail with attachment failed: " + e.getMessage()
            );
        }
    }
}
