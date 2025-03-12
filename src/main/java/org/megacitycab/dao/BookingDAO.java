package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Vehicle;
import org.megacitycab.model.Driver;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    // Insert booking record and return the generated BookingID
    public int addBooking(Booking booking) {
        String sql = "INSERT INTO booking (UserID, VehicleCategoryID, PickupAddress, DropoffAddress, BookingDate,BookingEndTime, DistanceKm, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        int generatedId = -1;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getUserID());
            stmt.setInt(2, booking.getVehicleCategoryID());
            stmt.setString(3, booking.getPickupAddress());
            stmt.setString(4, booking.getDropoffAddress());
            stmt.setTimestamp(5, booking.getBookingDate());
            stmt.setTimestamp(6, booking.getBookingEndTime());
            stmt.setDouble(7, booking.getDistanceKm());
            stmt.setString(8, booking.getStatus());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    // Update the booking status (used for cancellations)
    public boolean updateBookingStatus(int bookingID, String status) {
        String sql = "UPDATE booking SET Status = ? WHERE BookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all bookings for a specific customer
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE UserID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));
                    booking.setUserID(rs.getInt("UserID"));
                    booking.setVehicleCategoryID(rs.getInt("VehicleCategoryID"));
                    booking.setPickupAddress(rs.getString("PickupAddress"));
                    booking.setDropoffAddress(rs.getString("DropoffAddress"));
                    booking.setBookingDate(rs.getTimestamp("BookingDate"));
                    booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                    booking.setDistanceKm(rs.getDouble("DistanceKm"));
                    booking.setBookingMadeAt(rs.getTimestamp("BookingMadeAt"));
                    booking.setStatus(rs.getString("Status"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Get all pending bookings matching a vehicle category
    public List<Booking> getPendingBookingsByCategory(int categoryID) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE VehicleCategoryID = ? AND Status = 'Pending'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, categoryID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BookingID"));
                booking.setUserID(rs.getInt("UserID"));
                booking.setPickupAddress(rs.getString("PickupAddress"));
                booking.setDropoffAddress(rs.getString("DropoffAddress"));
                booking.setBookingDate(rs.getTimestamp("BookingDate"));
                booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                booking.setDistanceKm(rs.getDouble("DistanceKm"));
                booking.setStatus(rs.getString("Status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Accept a booking and assign a driver & vehicle
    public boolean acceptBooking(int bookingID, int driverID, int vehicleID) {
        String sql = "UPDATE booking SET DriverID = ?, VehicleID = ?, Status = 'Accepted' WHERE BookingID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            stmt.setInt(2, vehicleID);
            stmt.setInt(3, bookingID);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Booking> getAcceptedBookingsByDriverID(int driverID) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, u.Name FROM booking b " +
                "JOIN user u ON b.UserID = u.UserID " +  // Join with the user table based on UserID
                "WHERE b.DriverID = ? AND b.Status IN ('Accepted', 'In Progress', 'Cancelled', 'Completed')";  // Only select accepted bookings for the driver

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BookingID"));
                booking.setPickupAddress(rs.getString("PickupAddress"));
                booking.setDropoffAddress(rs.getString("DropoffAddress"));
                booking.setBookingDate(rs.getTimestamp("BookingDate"));
                booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                booking.setDistanceKm(rs.getDouble("DistanceKm"));
                booking.setStatus(rs.getString("Status"));

                // Retrieve the user's name from the joined table
                String userName = rs.getString("Name");
                booking.setUserName(userName); // Assuming Booking has a userName field to store this value

                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Get a single booking by its ID
    public Booking getBookingById(int bookingID) {
        Booking booking = null;
        String sql = "SELECT * FROM booking WHERE BookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));
                    booking.setUserID(rs.getInt("UserID"));
                    booking.setVehicleCategoryID(rs.getInt("VehicleCategoryID"));
                    booking.setPickupAddress(rs.getString("PickupAddress"));
                    booking.setDropoffAddress(rs.getString("DropoffAddress"));
                    booking.setBookingDate(rs.getTimestamp("BookingDate"));
                    booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                    booking.setDistanceKm(rs.getDouble("DistanceKm"));
                    booking.setBookingMadeAt(rs.getTimestamp("BookingMadeAt"));
                    booking.setStatus(rs.getString("Status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }


    public Booking getBookingDetailsWithDriverAndVehicle(int bookingID) {
        Booking booking = null;
        String sql = "SELECT b.*, d.Name AS DriverName, d.PhoneNumber AS DriverPhone, d.ImageURL AS DriverImage, " +
                "v.LicensePlate, v.Color " +
                "FROM booking b " +
                "LEFT JOIN driver d ON b.DriverID = d.DriverID " +
                "LEFT JOIN vehicle v ON b.VehicleID = v.VehicleID " +
                "WHERE b.BookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));

                    int driverId = rs.getInt("DriverID");
                    if (rs.wasNull()) {
                        driverId = 0; // or leave as is
                    }
                    booking.setDriverID(driverId);

                    int vehicleId = rs.getInt("VehicleID");
                    if (rs.wasNull()) {
                        vehicleId = 0; // or leave as is
                    }
                    booking.setVehicleID(vehicleId);

                    booking.setStatus(rs.getString("Status"));

                    // Set driver details if available
                    String driverName = rs.getString("DriverName");
                    if (driverName != null) {
                        Driver driver = new Driver();
                        driver.setName(driverName);
                        driver.setPhoneNumber(rs.getString("DriverPhone"));
                        driver.setImageURL(rs.getString("DriverImage"));
                        booking.setDriver(driver);
                    }

                    // Set vehicle details if available
                    String licensePlate = rs.getString("LicensePlate");
                    if (licensePlate != null) {
                        Vehicle vehicle = new Vehicle();
                        vehicle.setLicensePlate(licensePlate);
                        vehicle.setColor(rs.getString("Color"));
                        booking.setVehicle(vehicle);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    // Get all bookings (for the table view)
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while(rs.next()){
                Booking booking = new Booking();
                booking.setBookingID(rs.getInt("BookingID"));
                booking.setUserID(rs.getInt("UserID"));
                booking.setVehicleCategoryID(rs.getInt("VehicleCategoryID"));
                booking.setPickupAddress(rs.getString("PickupAddress"));
                booking.setDropoffAddress(rs.getString("DropoffAddress"));
                booking.setBookingDate(rs.getTimestamp("BookingDate"));
                booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                booking.setDistanceKm(rs.getDouble("DistanceKm"));
                booking.setBookingMadeAt(rs.getTimestamp("BookingMadeAt"));
                booking.setStatus(rs.getString("Status"));
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // Get bookings filtered by month (using the booking date)
    public List<Booking> getBookingsByMonth(String month) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE MONTHNAME(BookingDate) = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, month);
            try (ResultSet rs = stmt.executeQuery()) {
                while(rs.next()){
                    Booking booking = new Booking();
                    booking.setBookingID(rs.getInt("BookingID"));
                    booking.setUserID(rs.getInt("UserID"));
                    booking.setVehicleCategoryID(rs.getInt("VehicleCategoryID"));
                    booking.setPickupAddress(rs.getString("PickupAddress"));
                    booking.setDropoffAddress(rs.getString("DropoffAddress"));
                    booking.setBookingDate(rs.getTimestamp("BookingDate"));
                    booking.setBookingEndTime(rs.getTimestamp("BookingEndTime"));
                    booking.setDistanceKm(rs.getDouble("DistanceKm"));
                    booking.setBookingMadeAt(rs.getTimestamp("BookingMadeAt"));
                    booking.setStatus(rs.getString("Status"));
                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }


}


