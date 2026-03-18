package com.BookingService.BookingService.service;

import com.BookingService.BookingService.model.Booking;
import com.BookingService.BookingService.model.Trip;
import com.BookingService.BookingService.repository.BookingRepository;
import com.BookingService.BookingService.repository.TripRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class InvoiceService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripRepository tripRepository;

    public byte[] generateInvoice(Long tourId) {

        Booking booking = bookingRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Trip trip = tripRepository.findById(booking.getTripId())
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4, 40, 40, 40, 40);

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph title = new Paragraph("TripGenix Invoice", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Invoice Date: " + LocalDate.now()));
            document.add(new Paragraph("Invoice No: INV-" + booking.getReferenceId()));
            document.add(new Paragraph(" "));

            // Customer Info
            PdfPTable customerTable = new PdfPTable(2);
            customerTable.setWidthPercentage(100);
            customerTable.setSpacingBefore(10);

            customerTable.addCell(cell("Billed To:", true));
            customerTable.addCell(cell(booking.getBookerName(), false));

            customerTable.addCell(cell("Booking Reference:", true));
            customerTable.addCell(cell(booking.getReferenceId(), false));

            document.add(customerTable);
            document.add(new Paragraph(" "));

            // -------- PRICE CALCULATIONS --------
            BigDecimal total = trip.getEstimatedCost();

            BigDecimal paidAmount = total
                    .multiply(new BigDecimal("0.25"))
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal remainingAmount = total
                    .subtract(paidAmount)
                    .setScale(2, RoundingMode.HALF_UP);

            // -------- ITEM TABLE --------
            PdfPTable itemTable = new PdfPTable(4);
            itemTable.setWidthPercentage(100);
            itemTable.setWidths(new int[]{4, 2, 2, 2});

            itemTable.addCell(header("Description"));
            itemTable.addCell(header("Date"));
            itemTable.addCell(header("Passengers"));
            itemTable.addCell(header("Amount"));
            int passengers= booking.getAdults()+booking.getBabies() +booking.getChildren();
            itemTable.addCell("Tour Package");
            itemTable.addCell(trip.getStartDateTime().toLocalDate().toString());
            itemTable.addCell(String.valueOf(passengers));
            itemTable.addCell("LKR " + total);

            document.add(itemTable);
            document.add(new Paragraph(" "));

            // -------- PAYMENT SUMMARY --------
            PdfPTable paymentTable = new PdfPTable(2);
            paymentTable.setWidthPercentage(40);
            paymentTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            paymentTable.addCell(cell("Total Amount:", true));
            paymentTable.addCell(cell("LKR " + total, false));

            paymentTable.addCell(cell("Paid (25%):", true));
            paymentTable.addCell(cell("LKR " + paidAmount, false));

            paymentTable.addCell(cell("Remaining Amount:", true));
            paymentTable.addCell(cell("LKR " + remainingAmount, false));

            document.add(paymentTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph(
                    "Thank you for choosing TripGenix.\nThis invoice is system generated.",
                    FontFactory.getFont(FontFactory.HELVETICA, 10)
            ));

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Invoice generation failed", e);
        }

        return out.toByteArray();
    }

    private PdfPCell header(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(8);
        return cell;
    }

    private PdfPCell cell(String text, boolean bold) {
        Font font = bold
                ? FontFactory.getFont(FontFactory.HELVETICA_BOLD)
                : FontFactory.getFont(FontFactory.HELVETICA);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(8);
        return cell;
    }
}