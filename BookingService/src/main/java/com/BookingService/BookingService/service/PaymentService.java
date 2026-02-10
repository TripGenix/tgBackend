package com.BookingService.BookingService.service;

import com.BookingService.BookingService.dto.PaymentDto;
import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Payment;
import com.BookingService.BookingService.model.PaymentHistory;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.PaymentHistoryRepository;
import com.BookingService.BookingService.repository.PaymentRepository;
import com.BookingService.BookingService.repository.TripRepository;
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

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

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

//    @Transactional
//    public void handlePayHereNotification(HttpServletRequest request) {
//
//        String merchantId = request.getParameter("merchant_id");
//        String orderId = request.getParameter("order_id");
//        String paymentId = request.getParameter("payment_id");
//        String amount = request.getParameter("payhere_amount");
//        String currency = request.getParameter("payhere_currency");
//        String statusCode = request.getParameter("status_code");
//        String md5sig = request.getParameter("md5sig");
//
//        // 🔐 Step 1: Generate local MD5
//        String secretHash = getMd5(MERCHANT_SECRET);
//
//        String localMd5 = getMd5(
//                merchantId +
//                        orderId +
//                        amount +
//                        currency +
//                        statusCode +
//                        secretHash
//        );
//
//        if (!localMd5.equals(md5sig)) {
//            throw new RuntimeException("Invalid PayHere signature");
//        }
//
//        // 🔍 Find booking
//        Booking booking = (Booking) bookingRepository.findByReferenceId(orderId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        Payment payment = paymentRepository
//                .findByBookingId(booking.getBookingId())
//                .orElse(null);
//
//        if (payment == null) {
//            payment = new Payment();
//            payment.setBookingId(booking.getBookingId());
//            payment.setPaymentType("PAYHERE");
//        }
//
//        if ("PAID".equals(payment.getStatus())) {
//            return;
//        }
//
//        payment.setAmount(new BigDecimal(amount));
//        payment.setPaymentDateTime(LocalDateTime.now());
//
//        switch (statusCode) {
//            case "2":
//                payment.setStatus("PAID");
//                break;
//
//            case "0":
//                payment.setStatus("PENDING");
//                break;
//
//            case "-1":
//                payment.setStatus("CANCELLED");
//                break;
//
//            case "-2":
//                payment.setStatus("FAILED");
//                break;
//
//            case "-3":
//                payment.setStatus("CHARGEDBACK");
//                break;
//        }
//
//        paymentRepository.save(payment);
//    }

    @Transactional
    public void pay(PaymentDto dto) {

        // Get booking
        Booking booking = bookingRepository.findById(dto.getBookingId())
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        //  Get trip & total amount
        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        BigDecimal totalAmount = trip.getTotalCost();
        BigDecimal newPaidAmount = dto.getPaidAmount();

        if (newPaidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Paid amount must be greater than zero");
        }

        // 3️⃣ Find existing payment summary
        Payment payment = paymentRepository
                .findByBookingId(dto.getBookingId())
                .orElse(null);

        BigDecimal updatedPaidAmount;
        BigDecimal updatedBalance;
        String status;

        if (payment == null) {
            // 🔹 FIRST PAYMENT
            updatedPaidAmount = newPaidAmount;
        } else {
            // 🔁 ADD TO EXISTING PAYMENT
            updatedPaidAmount = payment.getPaidAmount().add(newPaidAmount);
        }

        updatedBalance = totalAmount.subtract(updatedPaidAmount);

        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Total paid amount exceeds trip total cost");
        }

        status = updatedBalance.compareTo(BigDecimal.ZERO) == 0
                ? "PAID"
                : "ADVANCED";

        // 4️⃣ Save / Update PAYMENT SUMMARY
        if (payment == null) {
            payment = new Payment();
            payment.setBookingId(dto.getBookingId());
        }

        payment.setPaidAmount(updatedPaidAmount);
        payment.setBalance(updatedBalance);
        payment.setStatus(status);
        payment.setPaymentType(dto.getPaymentType());
        payment.setPaymentDateTime(LocalDateTime.now());

        paymentRepository.save(payment);

        // 5️⃣ SAVE PAYMENT HISTORY (ALWAYS INSERT)
        PaymentHistory history = new PaymentHistory();
        history.setBookingId(dto.getBookingId());
        history.setPaidAmount(newPaidAmount);
        history.setPaymentType(dto.getPaymentType());
        history.setPaidDateTime(LocalDateTime.now());
        history.setStatus("SUCCESS");

        paymentHistoryRepository.save(history);
    }


}
