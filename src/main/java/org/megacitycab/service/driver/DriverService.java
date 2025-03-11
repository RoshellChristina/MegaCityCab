package org.megacitycab.service.driver;

import org.megacitycab.dao.BookingDAO;
import org.megacitycab.dao.DriverDAO;
import org.megacitycab.dao.VehicleDAO;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Driver;
import org.megacitycab.model.Vehicle;
import org.megacitycab.observer.BookingNotificationManager;

import java.util.List;

public class DriverService {
    private DriverDAO driverDAO = new DriverDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private VehicleDAO vehicleDAO = new VehicleDAO();


    public boolean addDriver(Driver driver) {
        if(driver.getEmpType() == null || driver.getEmpType().isEmpty()){
            driver.setEmpType("company_emp");
        }
        return driverDAO.addDriver(driver);
    }

    public boolean updateDriver(Driver driver) {
        return driverDAO.updateDriver(driver);
    }

    public boolean deleteDriver(int driverID) {
        return driverDAO.deleteDriver(driverID);
    }

    public List<Driver> getAllDrivers() {
        return driverDAO.getAllDrivers();
    }

    public Driver getDriverById(int driverID) {
        return driverDAO.getDriverById(driverID);
    }

   public Driver getDriverByUsername(String Username) {
        return driverDAO.getDriverByUsername(Username);
   }

    // Get pending bookings for the driver's vehicle category
    public List<Booking> getPendingBookingsForDriver(int driverID) {
        Vehicle vehicle = vehicleDAO.getVehicleByDriverId(driverID);
        if (vehicle != null) {
            return bookingDAO.getPendingBookingsByCategory(vehicle.getCategoryID());
        }
        return null;
    }

    // Accept a booking
    public boolean acceptBooking(int driverID, int bookingID) {

        // Retrieve the booking details
        Booking booking = bookingDAO.getBookingById(bookingID);
        if (booking == null) {
            return false; // Booking not found
        }

        // Check if the driver already has a booking that conflicts
        BookingConflictChecker conflictChecker = new DefaultBookingConflictChecker(bookingDAO);
        if (conflictChecker.hasConflict(driverID, booking.getBookingDate(), booking.getBookingEndTime())) {
            // Conflict exists; driver cannot accept this booking
            return false;
        }

        Vehicle vehicle = vehicleDAO.getVehicleByDriverId(driverID);
        if (vehicle != null) {
            boolean updated = bookingDAO.acceptBooking(bookingID, driverID, vehicle.getVehicleID());
            if (updated) {
                // Update status to "Accepted" (or any other status as needed)
                booking.setStatus("Accepted");
                // Notify the registered observer(s)
                BookingNotificationManager.getInstance().notifyObservers(booking);
                return true;
            }
        }
        return false;
    }


    // Method to get accepted bookings for the driver
    public List<Booking> getAcceptedBookingsForDriver(int driverID) {
        return bookingDAO.getAcceptedBookingsByDriverID(driverID);
    }

    public boolean updateBookingStatus(int driverID, int bookingID, String status) {
        boolean isUpdated = driverDAO.updateBookingStatus(driverID, bookingID, status);
        if (isUpdated) {
            Booking booking = bookingDAO.getBookingById(bookingID);
            if (booking != null) {
                booking.setStatus(status);

                // Add customer notification
                BookingNotificationManager.getInstance().addNotification(
                        booking.getUserID(),
                        "Your booking (ID: " + bookingID + ") status is now: " + status
                );

                // Notify observers (for live updates)
                BookingNotificationManager.getInstance().notifyObservers(booking);
            }
        }
        return isUpdated;
    }

    public Driver getDriverByID(int driverID) {
        return driverDAO.getDriverByID(driverID);
    }


}
