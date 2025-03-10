package org.megacitycab.observer;

import java.util.*;
import org.megacitycab.model.Booking;

public class BookingNotificationManager {
    private static BookingNotificationManager instance;
    // Map to store notifications per booking (if you want to notify immediately)
    private Map<Integer, List<BookingObserver>> observersMap;
    // Map to persist notifications per customer ID
    private Map<Integer, List<String>> customerNotifications;

    private BookingNotificationManager() {
        observersMap = new HashMap<>();
        customerNotifications = new HashMap<>();
    }


    public static BookingNotificationManager getInstance() {
        if (instance == null) {
            synchronized (BookingNotificationManager.class) {
                if (instance == null) {
                    instance = new BookingNotificationManager();
                }
            }
        }
        return instance;
    }

    // Observer registration (if you need real-time notifications while logged in)
    public void registerObserver(int bookingID, BookingObserver observer) {
        observersMap.computeIfAbsent(bookingID, k -> new ArrayList<>()).add(observer);
    }

    public void removeObserver(int bookingID, BookingObserver observer) {
        List<BookingObserver> observers = observersMap.get(bookingID);
        if (observers != null) {
            observers.remove(observer);
        }
    }

    // Notify observers when booking status changes.
    public void notifyObservers(Booking booking) {
        List<BookingObserver> observers = observersMap.get(booking.getBookingID());
        if (observers != null) {
            for (BookingObserver observer : observers) {
                observer.update(booking);
            }
        }
    }

    // --- Notification persistence methods ---
    public void addNotification(int customerId, String message) {
        List<String> notifications = customerNotifications.get(customerId);
        if (notifications == null) {
            notifications = new ArrayList<>();
            customerNotifications.put(customerId, notifications);
        }
        notifications.add(message);
    }

    public List<String> getNotificationsForCustomer(int customerId) {
        return customerNotifications.getOrDefault(customerId, new ArrayList<>());
    }

    public void clearNotificationsForCustomer(int customerId) {
        customerNotifications.remove(customerId);
    }
}

