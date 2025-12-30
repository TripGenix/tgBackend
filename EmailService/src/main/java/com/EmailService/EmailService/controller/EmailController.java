package com.EmailService.EmailService.controller;

import com.EmailService.EmailService.Dto.EmailDetailsDto;
import com.EmailService.EmailService.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/email/api/v1")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public CompletableFuture<String> sendMail(
            @RequestBody EmailDetailsDto details) {

        return emailService.sendSimpleMail(details);
    }

    @PostMapping("/send-with-attachment")
    public CompletableFuture<String> sendMailWithAttachment(
            @RequestBody EmailDetailsDto details) {

        return emailService.sendMailWithAttachment(details);
    }
}
