package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.service.customer.PaymentService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.megacitycab.dao.PaymentDAO;
import org.megacitycab.model.Payment;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private PaymentDAO paymentDAOMock;

    @Before
    public void setUp() throws Exception {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        paymentService = new PaymentService();

        // Inject the PaymentDAO mock into PaymentService using reflection
        Field daoField = paymentService.getClass().getDeclaredField("paymentDAO");
        daoField.setAccessible(true);
        daoField.set(paymentService, paymentDAOMock);
    }

    @Test
    public void testCreatePayment_success() {
        int bookingID = 100;
        int userID = 200;
        double amount = 150.75;
        String paymentMethod = "Credit Card";

        // Simulate a successful DAO operation
        when(paymentDAOMock.addPayment(any(Payment.class))).thenReturn(true);

        boolean result = paymentService.createPayment(bookingID, userID, amount, paymentMethod);
        assertTrue(result);

        // Capture the Payment object passed to addPayment and verify its fields
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentDAOMock, times(1)).addPayment(paymentCaptor.capture());

        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(bookingID, capturedPayment.getBookingID());
        assertEquals(userID, capturedPayment.getUserID());
        assertEquals(amount, capturedPayment.getAmount(), 0.001);
        assertEquals(paymentMethod, capturedPayment.getPaymentMethod());
        assertEquals("Completed", capturedPayment.getStatus());
    }

    @Test
    public void testCreatePayment_failure() {
        int bookingID = 101;
        int userID = 201;
        double amount = 200.00;
        String paymentMethod = "Debit Card";

        // Simulate a failure in DAO call
        when(paymentDAOMock.addPayment(any(Payment.class))).thenReturn(false);

        boolean result = paymentService.createPayment(bookingID, userID, amount, paymentMethod);
        assertFalse(result);

        // Optionally capture and verify the Payment object
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentDAOMock, times(1)).addPayment(paymentCaptor.capture());

        Payment capturedPayment = paymentCaptor.getValue();
        assertEquals(bookingID, capturedPayment.getBookingID());
        assertEquals(userID, capturedPayment.getUserID());
        assertEquals(amount, capturedPayment.getAmount(), 0.001);
        assertEquals(paymentMethod, capturedPayment.getPaymentMethod());
        assertEquals("Completed", capturedPayment.getStatus());
    }

    @Test
    public void testUpdatePaymentStatus() {
        int bookingID = 102;
        String newStatus = "Refunded";

        // Simulate a successful update
        when(paymentDAOMock.updatePaymentStatus(bookingID, newStatus)).thenReturn(true);

        boolean result = paymentService.updatePaymentStatus(bookingID, newStatus);
        assertTrue(result);
        verify(paymentDAOMock, times(1)).updatePaymentStatus(bookingID, newStatus);
    }

    @Test
    public void testGetPaymentByBookingId() {
        int bookingID = 103;
        Payment payment = new Payment();
        payment.setBookingID(bookingID);
        payment.setUserID(300);
        payment.setAmount(99.99);
        payment.setPaymentMethod("PayPal");
        payment.setStatus("Completed");

        when(paymentDAOMock.getPaymentByBookingId(bookingID)).thenReturn(payment);

        Payment result = paymentService.getPaymentByBookingId(bookingID);
        assertNotNull(result);
        assertEquals(bookingID, result.getBookingID());
        verify(paymentDAOMock, times(1)).getPaymentByBookingId(bookingID);
    }
}
