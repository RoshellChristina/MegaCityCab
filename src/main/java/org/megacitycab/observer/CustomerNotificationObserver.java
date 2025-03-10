package org.megacitycab.observer;

import org.megacitycab.model.Booking;

public class CustomerNotificationObserver implements BookingObserver {
    private int customerID;

    public CustomerNotificationObserver(int customerID) {
        this.customerID = customerID;
    }

    @Override
    public void update(Booking booking) {
        // Create the notification message.
        String message = "Your booking (ID: " + booking.getBookingID()
                + ") status changed to: " + booking.getStatus();
        // Save the notification for the customer.
        BookingNotificationManager.getInstance().addNotification(customerID, message);
        System.out.println("Notification stored for customer " + customerID + ": " + message);
    }
}


