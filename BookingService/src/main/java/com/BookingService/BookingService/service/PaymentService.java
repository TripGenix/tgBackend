package com.BookingService.BookingService.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
@Service
public class PaymentService {

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
}
