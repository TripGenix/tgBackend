package com.BookingService.BookingService.EmailTemplates;

import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseById;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;

public class EmailTotalCostPay {

    public static String buildTotalPaymentEmail(
            BookingSystemResponseById booking,
            String paymentUrl
    ) {

        BigDecimal totalCost = BigDecimal.valueOf(booking.getRouteDetails().getBookingPrice());

        // 25% already paid
        BigDecimal advancePayment = totalCost
                .multiply(BigDecimal.valueOf(0.25))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal remainingBalance = totalCost
                .subtract(advancePayment)
                .setScale(2, RoundingMode.HALF_UP);

        return """
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>TripGenix Balance Payment</title>
</head>

<body style="margin:0; padding:0; background-color:#f3f4f6; font-family:Arial, Helvetica, sans-serif;">
  <table width="100%%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:30px 15px;">

        <table width="100%%" cellpadding="0" cellspacing="0"
               style="max-width:620px; background:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.08);">

          <!-- HEADER -->
          <tr>
            <td style="background:linear-gradient(135deg,#16a34a,#065f46); padding:30px; text-align:center;">
              <h1 style="margin:0; color:#ffffff; font-size:28px;">TripGenix</h1>
              <p style="margin:6px 0 0; color:#bbf7d0; font-size:14px;">
                Complete Your Journey Payment
              </p>
            </td>
          </tr>

          <!-- BODY -->
          <tr>
            <td style="padding:30px; color:#374151; font-size:15px;">

              <p>Dear <b>%s</b>,</p>

              <p>
                Your trip is approaching soon! 🎉
              </p>

              <p>
                We kindly remind you to complete your <b>remaining balance payment</b>
                to ensure a smooth and hassle-free journey.
              </p>

              <!-- BOOKING DETAILS -->
              <table width="100%%" cellpadding="0" cellspacing="0"
                     style="background:#f9fafb; border-radius:10px; padding:20px; margin:20px 0;">
                
                <tr>
                  <td><b>Booking Reference</b></td>
                  <td>%s</td>
                </tr>

                <tr>
                  <td><b>Route</b></td>
                  <td>%s</td>
                </tr>

                <tr>
                  <td><b>Total Cost</b></td>
                  <td>LKR %s</td>
                </tr>

                <tr>
                  <td><b>Advance Paid (25%%)</b></td>
                  <td style="color:#16a34a;">LKR %s</td>
                </tr>

                <tr>
                  <td><b>Remaining Balance</b></td>
                  <td style="font-weight:600; color:#dc2626;">
                    LKR %s
                  </td>
                </tr>

              </table>

              <p style="font-size:14px; color:#374151;">
                ⚠️ Please complete your remaining payment before your travel date
                to avoid any inconvenience.
              </p>

              <!-- PAYMENT BUTTON -->
              <div style="text-align:center; margin:30px 0;">
                <a href="%s"
                   style="
                     display:inline-block;
                     min-width:220px;
                     background:#16a34a;
                     color:#ffffff;
                     padding:16px 0;
                     border-radius:8px;
                     text-decoration:none;
                     font-weight:600;
                     font-size:16px;
                   ">
                  💳 Pay Remaining Balance
                </a>
              </div>

              <p>
                Thank you for choosing <b>TripGenix</b>.<br/>
                We wish you a wonderful trip! 🌍
              </p>

              <p style="margin-top:30px;">
                Best regards,<br/>
                <b>TripGenix Team</b>
              </p>

            </td>
          </tr>

          <!-- FOOTER -->
          <tr>
            <td style="background:#f3f4f6; padding:18px; text-align:center; font-size:12px; color:#6b7280;">
              © %d TripGenix. All rights reserved.
            </td>
          </tr>

        </table>

      </td>
    </tr>
  </table>
</body>
</html>
"""
                .formatted(
                        booking.getBookingDetails().getNameOfBooker(),
                        booking.getReferenceId(),
                        String.join(" → ", booking.getTripDetails().getDestinations()),
                        totalCost.toPlainString(),
                        advancePayment.toPlainString(),
                        remainingBalance.toPlainString(),
                        paymentUrl,
                        Year.now().getValue()
                );
    }
}