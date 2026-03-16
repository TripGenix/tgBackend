package com.EmailService.EmailService.service;

import com.EmailService.EmailService.Dto.EmailDetailsDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
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

    /**
     * ✅ Send HTML Email (NO attachment)
     */
    @Override
    @Async
    public CompletableFuture<String> sendSimpleMail(EmailDetailsDto details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());

            // ✅ HTML ENABLED (IMPORTANT)
            helper.setText(details.getMsgBody(), true);

            javaMailSender.send(mimeMessage);

            return CompletableFuture.completedFuture("Mail sent successfully");
        } catch (MessagingException e) {
            return CompletableFuture.completedFuture(
                    "Mail sending failed: " + e.getMessage()
            );
        }
    }

    /**
     * ✅ Send HTML Email WITH attachment
     */
    @Override
    @Async
    public CompletableFuture<String> sendMailWithAttachment(EmailDetailsDto details) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());

            // ✅ HTML ENABLED (IMPORTANT)
            helper.setText(details.getMsgBody(), true);

            // ✅ Attach file if exists
            if (details.getAttachment() != null && !details.getAttachment().isEmpty()) {
                FileSystemResource file =
                        new FileSystemResource(new File(details.getAttachment()));
                helper.addAttachment(file.getFilename(), file);
            }

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
