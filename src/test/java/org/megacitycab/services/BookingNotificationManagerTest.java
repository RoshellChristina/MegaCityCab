package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.megacitycab.model.Booking;
import org.megacitycab.observer.*;

import java.util.List;

public class BookingNotificationManagerTest {

    private BookingNotificationManager notificationManager;
    private BookingObserver mockObserver;
    private Booking testBooking;

    @Before
    public void setUp() {
        // Setup BookingNotificationManager and mock observer
        notificationManager = BookingNotificationManager.getInstance();
        mockObserver = mock(BookingObserver.class);

        // Mock a test booking
        testBooking = new Booking();
        testBooking.setBookingID(1);
        testBooking.setStatus("Confirmed");
    }

    @Test
    public void testRegisterObserver() {
        // Register observer for the booking
        notificationManager.registerObserver(testBooking.getBookingID(), mockObserver);

        // Verify that the observer has been added for the booking
        notificationManager.notifyObservers(testBooking);

        // Verify that the update method of the observer was called
        verify(mockObserver).update(testBooking);
    }

    @Test
    public void testRemoveObserver() {
        // Register observer
        notificationManager.registerObserver(testBooking.getBookingID(), mockObserver);

        // Remove observer
        notificationManager.removeObserver(testBooking.getBookingID(), mockObserver);

        // Notify observers (should not call the update method since observer is removed)
        notificationManager.notifyObservers(testBooking);

        // Verify that the observer's update method is not called
        verify(mockObserver, never()).update(testBooking);
    }

    @Test
    public void testAddNotification() {
        // Create a customer ID
        int customerID = 101;

        // Add notification for the customer
        notificationManager.addNotification(customerID, "Your booking has been confirmed.");

        // Fetch notifications for the customer
        List<String> notifications = notificationManager.getNotificationsForCustomer(customerID);

        // Assert that the notification was added
        assertEquals(1, notifications.size());
        assertEquals("Your booking has been confirmed.", notifications.get(0));
    }

    @Test
    public void testClearNotifications() {
        // Create a customer ID
        int customerID = 101;

        // Add notifications for the customer
        notificationManager.addNotification(customerID, "Your booking has been confirmed.");
        notificationManager.addNotification(customerID, "Your booking is on the way.");

        // Clear notifications for the customer
        notificationManager.clearNotificationsForCustomer(customerID);

        // Verify that notifications are cleared
        List<String> notifications = notificationManager.getNotificationsForCustomer(customerID);
        assertTrue(notifications.isEmpty());
    }

    @Test
    public void testNotificationStoredForCustomer() {
        // Create a customer ID
        int customerID = 101;

        // Create a CustomerNotificationObserver for the customer
        CustomerNotificationObserver customerObserver = new CustomerNotificationObserver(customerID);

        // Register the observer for the test booking
        notificationManager.registerObserver(testBooking.getBookingID(), customerObserver);

        // Notify observers
        notificationManager.notifyObservers(testBooking);

        // Fetch notifications for the customer
        List<String> notifications = notificationManager.getNotificationsForCustomer(customerID);

        // Assert that the notification was stored
        assertEquals(1, notifications.size());
        assertTrue(notifications.get(0).contains("Your booking (ID: 1) status changed to: Confirmed"));
    }
}
