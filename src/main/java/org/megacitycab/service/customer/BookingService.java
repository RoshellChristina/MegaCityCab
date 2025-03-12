package org.megacitycab.service.customer;

import org.megacitycab.dao.BookingDAO;
import org.megacitycab.model.Booking;
import org.megacitycab.model.VehicleCategory;
import org.megacitycab.observer.BookingNotificationManager;
import org.megacitycab.observer.CustomerNotificationObserver;
import org.megacitycab.service.admin.VehicleCategoryService;
import org.megacitycab.service.customer.strategy.BookingEndTimeStrategy;
import org.megacitycab.service.customer.strategy.DefaultBookingEndTimeStrategy;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO = new BookingDAO();
    private VehicleCategoryService vehicleCategoryService = new VehicleCategoryService();
    private BookingEndTimeStrategy endTimeStrategy = new DefaultBookingEndTimeStrategy();

    // Creates a new booking record and returns the generated bookingID
    public int createBooking(int userId, int vehicleCategoryID, String pickupAddress, String dropoffAddress, Timestamp bookingDate, double distanceKm) {

        Timestamp bookingMadeAt = Timestamp.from(Instant.now());

        // Ensure booking is at least 30 minutes in the future
        if (bookingDate.before(Timestamp.from(bookingMadeAt.toInstant().plus(Duration.ofMinutes(30))))) {
            return -2; // Special error code for "booking too soon"
        }
        // Get the category to retrieve the price
        VehicleCategory category = vehicleCategoryService.getCategoryById(vehicleCategoryID);
        if (category == null) {
            return -1; // or throw an exception
        }
        Booking booking = new Booking();
        booking.setUserID(userId);
        booking.setVehicleCategoryID(vehicleCategoryID);
        booking.setPickupAddress(pickupAddress);
        booking.setDropoffAddress(dropoffAddress);
        booking.setBookingDate(bookingDate);
        booking.setDistanceKm(distanceKm);
        booking.setStatus("Pending");

        // Calculate and set booking end time using the strategy
        Timestamp bookingEndTime = endTimeStrategy.calculateEndTime(bookingDate, distanceKm);
        booking.setBookingEndTime(bookingEndTime);

        int bookingID = bookingDAO.addBooking(booking);
        if (bookingID > 0) {
            booking.setBookingID(bookingID);
            // Register an observer for this booking
            BookingNotificationManager.getInstance().registerObserver(bookingID, new CustomerNotificationObserver(userId));
        }
        return bookingID;
    }

    // Cancel an existing booking (updates status to "Cancelled")
    public boolean cancelBooking(int bookingID) {
        return bookingDAO.updateBookingStatus(bookingID, "Cancelled");
    }

    public boolean markBookingPaid(int bookingID) {
        return bookingDAO.updateBookingStatus(bookingID, "Paid");
    }

    // Retrieve all bookings for a given customer
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    public Booking getBookingById(int bookingID) {
        return bookingDAO.getBookingById(bookingID);
    }
    public Booking getBookingDetailsWithDriverAndVehicle(int bookingID) {
        return bookingDAO.getBookingDetailsWithDriverAndVehicle(bookingID);
    }

    private boolean isPeakHour(Timestamp bookingDate) {
        int hour = bookingDate.toLocalDateTime().getHour();
        return (hour >= 7 && hour < 10) || (hour >= 17 && hour < 20);
    }



}
