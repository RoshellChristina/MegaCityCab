package org.megacitycab.service.driver;

import java.sql.Timestamp;
import java.util.List;
import org.megacitycab.dao.BookingDAO;
import org.megacitycab.model.Booking;

public class DefaultBookingConflictChecker implements BookingConflictChecker {
    private BookingDAO bookingDAO;

    public DefaultBookingConflictChecker(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }


    @Override
    public boolean hasConflict(int driverID, Timestamp newBookingStart, Timestamp newBookingEnd) {
        // Retrieve the driver's accepted bookings
        List<Booking> acceptedBookings = bookingDAO.getAcceptedBookingsByDriverID(driverID);
        for (Booking booking : acceptedBookings) {
            Timestamp existingStart = booking.getBookingDate();
            Timestamp existingEnd = booking.getBookingEndTime();

            // Check if the new booking overlaps with the existing booking
            if (!(newBookingEnd.before(existingStart) || newBookingStart.after(existingEnd))) {
                return true; // Conflict detected
            }
        }
        return false;
    }

}
