package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.service.InvoiceService;
import com.BookingService.BookingService.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentcontroller")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("getHash")
    public String getHash(
            @RequestParam String orderId,
            @RequestParam Double amount,
            @RequestParam String currency
    ) {
        System.out.println(orderId + "-" + amount + "-" + currency);
        return paymentService.generateHash(orderId, amount, currency);
    }

    @GetMapping("/invoice/{tourId}")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable Long tourId) {

        byte[] pdf = invoiceService.generateInvoice(tourId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=TripGenix-Invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @PostMapping("/notify")
    public ResponseEntity<String> paymentNotify(HttpServletRequest request) {

//        System.out.println(request.getRequestURI());
//        paymentService.handlePayHereNotification(request);
        return ResponseEntity.ok("OK");
    }

}
