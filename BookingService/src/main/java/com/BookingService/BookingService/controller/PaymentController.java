package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentcontroller")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("getHash")
    public String getHash(
            @RequestParam String orderId,
            @RequestParam Double amount,
            @RequestParam String currency
    ) {
        System.out.println(orderId + "-" + amount + "-" + currency);
        return paymentService.generateHash(orderId, amount, currency);
    }
}
