package org.megacitycab.service.customer;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Payment;

import java.io.ByteArrayOutputStream;

public class ReceiptService {

    private static BookingService bookingService = new BookingService();
    private static PaymentService paymentService = new PaymentService();

    /**
     * Generates a PDF receipt for the given booking ID.
     *
     * @param bookingID the booking ID
     * @return a byte array containing the PDF data
     * @throws IllegalArgumentException if the booking is not found
     */
    public static byte[] generateReceipt(int bookingID) {
        // Retrieve booking details using your service.
        Booking booking = bookingService.getBookingById(bookingID);
        if (booking == null) {
            throw new IllegalArgumentException("Invalid booking ID: " + bookingID);
        }

        // Retrieve payment details using PaymentService.
        Payment payment = paymentService.getPaymentByBookingId(bookingID);

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // Initialize PdfWriter with the document and output stream.
            PdfWriter.getInstance(document, baos);
            document.open();

            // Add booking details to the PDF.
            document.add(new Paragraph("Receipt for Booking: " + booking.getBookingID()));
            document.add(new Paragraph("Vehicle Category ID: " + booking.getVehicleCategoryID()));
            document.add(new Paragraph("Pickup Address: " + booking.getPickupAddress()));
            document.add(new Paragraph("Dropoff Address: " + booking.getDropoffAddress()));
            document.add(new Paragraph("Booking Date: " + booking.getBookingDate()));
            if (booking.getBookingEndTime() != null) {
                document.add(new Paragraph("Booking End Time: " + booking.getBookingEndTime()));
            }
            document.add(new Paragraph("Distance (Km): " + booking.getDistanceKm()));
            document.add(new Paragraph("Status: " + booking.getStatus()));

            // Separator
            document.add(new Paragraph("------------------------------------------------------"));

            // Add payment details if available.
            if (payment != null) {
                document.add(new Paragraph("Payment Details:"));
                document.add(new Paragraph("Amount: " + payment.getAmount()));
                document.add(new Paragraph("Payment Method: " + payment.getPaymentMethod()));
                document.add(new Paragraph("Payment Status: " + payment.getStatus()));
                document.add(new Paragraph("Payment Date: " + payment.getPaymentDate()));
            } else {
                document.add(new Paragraph("No payment record found."));
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }
}
