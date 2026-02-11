package com.BookingService.BookingService.controller;

import com.BookingService.BookingService.dto.PaymentDto;
import com.BookingService.BookingService.dto.systemReponse.DriverPaymentsResponseDto;
import com.BookingService.BookingService.dto.systemReponse.VehiclePaymentsResponseDto;
import com.BookingService.BookingService.model.CompanyEarn;
import com.BookingService.BookingService.model.DriverPayments;
import com.BookingService.BookingService.model.VehiclePayments;
import com.BookingService.BookingService.repository.CompanyEarnRepository;
import com.BookingService.BookingService.repository.DriverPaymentsRepository;
import com.BookingService.BookingService.repository.VehiclePaymentsRepository;
import com.BookingService.BookingService.service.InvoiceService;
import com.BookingService.BookingService.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paymentcontroller")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private CompanyEarnRepository companyEarnRepository;
    @Autowired
    private DriverPaymentsRepository driverPaymentsRepository;
    @Autowired
    private VehiclePaymentsRepository vehiclePaymentsRepository;

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

    @PostMapping("/pay")
    public ResponseEntity<String> pay(@RequestBody PaymentDto paymentDto) {
        paymentService.pay(paymentDto);
        return ResponseEntity.ok("OK");
    }



    @GetMapping("/companyEarn")
    public ResponseEntity<List<CompanyEarn>> getAllCompanyEarn() {

        List<CompanyEarn> earns = companyEarnRepository.findAll();

        return ResponseEntity.ok(earns);
    }

    @GetMapping("/driver-payments")
    public ResponseEntity<List<DriverPaymentsResponseDto>> getAllDriverPayments() {

        List<DriverPaymentsResponseDto> DriverPaymentsResponseDto = paymentService.getDriverPayments();

        return ResponseEntity.ok(DriverPaymentsResponseDto);
    }

    @GetMapping("/vehicel-payments")
    public ResponseEntity<List<VehiclePaymentsResponseDto>> getAllVehiclePayments() {

        List<VehiclePaymentsResponseDto> payments = paymentService.getVehiclePayments();

        return ResponseEntity.ok(payments);
    }


}
