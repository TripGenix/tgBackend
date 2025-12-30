package com.EmailService.EmailService.service;

import com.EmailService.EmailService.Dto.EmailDetailsDto;

import java.util.concurrent.CompletableFuture;

public interface EmailService {

    CompletableFuture<String> sendSimpleMail(EmailDetailsDto details);

    CompletableFuture<String> sendMailWithAttachment(EmailDetailsDto details);
}
