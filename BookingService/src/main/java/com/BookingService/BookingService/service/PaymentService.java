package com.BookingService.BookingService.service;

import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Payment;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.PaymentRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private BookingRepository bookingRepository;

    private static final String MERCHANT_ID = "1233436";

    @Value("${merchant.secret}")
    private String MERCHANT_SECRET; // SANDBOX SECRET

    public String generateHash(String orderId, Double amount, String currency) {

        DecimalFormat df = new DecimalFormat("0.00");
        String amountFormatted = df.format(amount);

        String rawString =
                MERCHANT_ID +
                        orderId +
                        amountFormatted +
                        currency +
                        getMd5(MERCHANT_SECRET); // already uppercase

        String hash = getMd5(rawString);

        System.out.println("Hash Input: " + rawString);
        System.out.println("Generated Hash: " + hash);

        return hash;
    }

    private static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext.toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void handlePayHereNotification(HttpServletRequest request) {

        String merchantId = request.getParameter("merchant_id");
        String orderId = request.getParameter("order_id");
        String paymentId = request.getParameter("payment_id");
        String amount = request.getParameter("payhere_amount");
        String currency = request.getParameter("payhere_currency");
        String statusCode = request.getParameter("status_code");
        String md5sig = request.getParameter("md5sig");

        // 🔐 Step 1: Generate local MD5
        String secretHash = getMd5(MERCHANT_SECRET);

        String localMd5 = getMd5(
                merchantId +
                        orderId +
                        amount +
                        currency +
                        statusCode +
                        secretHash
        );

        if (!localMd5.equals(md5sig)) {
            throw new RuntimeException("Invalid PayHere signature");
        }

        // 🔍 Find booking
        Booking booking = (Booking) bookingRepository.findByReferenceId(orderId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = paymentRepository
                .findByBookingId(booking.getBookingId())
                .orElse(null);

        if (payment == null) {
            payment = new Payment();
            payment.setBookingId(booking.getBookingId());
            payment.setPaymentType("PAYHERE");
        }

        if ("PAID".equals(payment.getStatus())) {
            return;
        }

        payment.setAmount(new BigDecimal(amount));
        payment.setPaymentDateTime(LocalDateTime.now());

        switch (statusCode) {
            case "2":
                payment.setStatus("PAID");
                break;

            case "0":
                payment.setStatus("PENDING");
                break;

            case "-1":
                payment.setStatus("CANCELLED");
                break;

            case "-2":
                payment.setStatus("FAILED");
                break;

            case "-3":
                payment.setStatus("CHARGEDBACK");
                break;
        }

        paymentRepository.save(payment);
    }
}
