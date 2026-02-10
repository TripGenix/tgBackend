package com.BookingService.BookingService.EmailTemplates;

import com.BookingService.BookingService.dto.systemReponse.BookingSystemResponseById;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;

public class EmailTemplateBuilder {

    public static String buildConfirmationEmail(
            BookingSystemResponseById booking,
            String paymentUrl,
            String cancelUrl
    ) {

        BigDecimal totalCost = BigDecimal.valueOf(booking.getRouteDetails().getBookingPrice());

        // 25% advance payment
        BigDecimal advancePayment = totalCost
                .multiply(BigDecimal.valueOf(0.25))
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal balancePayment = totalCost
                .subtract(advancePayment)
                .setScale(2, RoundingMode.HALF_UP);

        return """
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8" />
  <title>TripGenix Booking Confirmation</title>
</head>

<body style="margin:0; padding:0; background-color:#f3f4f6; font-family:Arial, Helvetica, sans-serif;">
  <table width="100%%" cellpadding="0" cellspacing="0">
    <tr>
      <td align="center" style="padding:30px 15px;">

        <table width="100%%" cellpadding="0" cellspacing="0"
               style="max-width:620px; background:#ffffff; border-radius:14px; overflow:hidden; box-shadow:0 10px 30px rgba(0,0,0,0.08);">

          <!-- HEADER -->
          <tr>
            <td style="background:linear-gradient(135deg,#2563eb,#1e40af); padding:30px; text-align:center;">
              <h1 style="margin:0; color:#ffffff; font-size:28px;">TripGenix</h1>
              <p style="margin:6px 0 0; color:#c7d2fe; font-size:14px;">
                Smart Travel • Seamless Journeys
              </p>
            </td>
          </tr>

          <!-- BODY -->
          <tr>
            <td style="padding:30px; color:#374151; font-size:15px;">

              <p>Dear <b>%s</b>,</p>

              <p>
                We’re happy to inform you that your tour booking has been
                <b style="color:#16a34a;">successfully confirmed</b>.
              </p>

              <p>
                🚗 <b>Your assigned driver has confirmed the tour</b>, and all arrangements
                have now been finalized.
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
                  <td><b>Travel Dates</b></td>
                  <td>%s → %s</td>
                </tr>

                <tr>
                  <td><b>Total Tour Cost</b></td>
                  <td style="font-weight:600; color:#1d4ed8;">
                    LKR %s
                  </td>
                </tr>

                <tr>
                <td><b>Advance Payment (25%%)</b></td>
                                                                                    <td style="font-weight:600; color:#16a34a;">
                    LKR %s
                  </td>
                </tr>

                <tr>
                  <td><b>Remaining Balance</b></td>
                  <td>
                    LKR %s
                  </td>
                </tr>
              </table>

             <p style="font-size:14px; color:#b91c1c; font-weight:600;">
                                    ⚠️ The advance payment of <b>25%%</b> is <u>NON-REFUNDABLE</u> once the booking is confirmed.
                                  </p>
                

              <p style="font-size:14px; color:#374151;">
                ⏳ Please complete the advance payment within <b>2 days</b>
                to secure your booking. The remaining balance can be settled later
                as per TripGenix policy.
              </p>

              <!-- BUTTONS -->
              <div style="text-align:center; margin:30px 0;">

                <a href="%s"
                   style="
                     display:inline-block;
                     min-width:220px;
                     background:#ef4444;
                     color:#ffffff;
                     padding:16px 0;
                     margin:6px;
                     border-radius:8px;
                     text-decoration:none;
                     font-weight:600;
                     font-size:16px;
                     text-align:center;
                   ">
                  Cancel Tour
                </a>

                <a href="%s"
                   style="
                     display:inline-block;
                     min-width:220px;
                     background:#2563eb;
                     color:#ffffff;
                     padding:16px 0;
                     margin:6px;
                     border-radius:8px;
                     text-decoration:none;
                     font-weight:600;
                     font-size:16px;
                     text-align:center;
                   ">
                  💳 Pay 25%% Advance
                </a>

              </div>

              <p>
                Thank you for choosing <b>TripGenix</b>.<br/>
                We look forward to providing you with a smooth and unforgettable journey.
              </p>

              <p style="margin-top:30px;">
                Warm regards,<br/>
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
                        booking.getTripDetails().getStartDate(),
                        booking.getTripDetails().getEndDate(),
                        totalCost.toPlainString(),
                        advancePayment.toPlainString(),
                        balancePayment.toPlainString(),
                        cancelUrl,
                        paymentUrl,
                        Year.now().getValue()
                );
    }
}
