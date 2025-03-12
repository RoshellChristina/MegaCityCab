package org.megacitycab.services;

import com.itextpdf.text.DocumentException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.megacitycab.service.customer.BookingService;
import org.megacitycab.service.customer.PaymentService;
import org.megacitycab.service.customer.ReceiptService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Payment;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReceiptServiceTest {

    @Mock
    private BookingService bookingService;

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private ReceiptService receiptService;

    private Booking booking;
    private Payment payment;

    @Before
    public void setUp() {
        // Setup mock objects
        booking = new Booking();
        booking.setBookingID(1);
        booking.setVehicleCategoryID(2);
        booking.setPickupAddress("123 Main St");
        booking.setDropoffAddress("456 Elm St");
        booking.setBookingDate(null); // Set as needed
        booking.setBookingEndTime(null); // Set as needed
        booking.setDistanceKm(10);
        booking.setStatus("Completed");

        payment = new Payment();
        payment.setAmount(100.0);
        payment.setPaymentMethod("Credit Card");
        payment.setStatus("Completed");
        payment.setPaymentDate(null); // Set as needed

        // Mock the services to return the mock objects
        when(bookingService.getBookingById(1)).thenReturn(booking);
        when(paymentService.getPaymentByBookingId(1)).thenReturn(payment);
    }

    @Test
    public void testGenerateReceiptValidBooking() {
        try {
            // Generate the receipt
            byte[] pdfData = receiptService.generateReceipt(1);
            assertNotNull(pdfData); // Ensure the PDF data is not null
            assertTrue(pdfData.length > 0); // Ensure the PDF data is not empty
        } catch (IllegalArgumentException e) {

        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGenerateReceiptInvalidBooking() {
        // When the booking is not found, it should throw an IllegalArgumentException
        when(bookingService.getBookingById(999)).thenReturn(null);
        receiptService.generateReceipt(999);
    }

    @Test
    public void testGenerateReceiptNoPayment() {
        // Modify the mock behavior to return null for payment
        when(paymentService.getPaymentByBookingId(1)).thenReturn(null);

        try {
            // Generate the receipt
            byte[] pdfData = receiptService.generateReceipt(1);
            assertNotNull(pdfData); // Ensure the PDF data is not null
            assertTrue(pdfData.length > 0); // Ensure the PDF data is not empty
        } catch (IllegalArgumentException e) {

        }
    }
}
