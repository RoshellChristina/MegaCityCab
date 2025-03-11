package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Driver;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DriverDAO {

    // Add a new driver and set its generated key
    public boolean addDriver(Driver driver) {
        String sql = "INSERT INTO driver (Emp_type, Name, Duty_Status, ImageURL, Username, Email, PhoneNumber, PasswordHash) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, driver.getEmpType());
            stmt.setString(2, driver.getName());
            stmt.setString(3, driver.getDutyStatus());
            stmt.setString(4, driver.getImageURL());
            stmt.setString(5, driver.getUsername());
            stmt.setString(6, driver.getEmail());
            stmt.setString(7, driver.getPhoneNumber());
            stmt.setString(8, driver.getPasswordHash());
            if (stmt.executeUpdate() > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        driver.setDriverID(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update driver details
    public boolean updateDriver(Driver driver) {
        String sql = "UPDATE driver SET Name = ?, Duty_Status = ?, ImageURL = ?, Username = ?, Email = ?, PhoneNumber = ?, PasswordHash = ? WHERE DriverID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getName());
            stmt.setString(2, driver.getDutyStatus());
            stmt.setString(3, driver.getImageURL());
            stmt.setString(4, driver.getUsername());
            stmt.setString(5, driver.getEmail());
            stmt.setString(6, driver.getPhoneNumber());
            stmt.setString(7, driver.getPasswordHash());
            stmt.setInt(8, driver.getDriverID());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete driver
    public boolean deleteDriver(int driverID) {
        String sql = "DELETE FROM driver WHERE DriverID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all drivers
    public List<Driver> getAllDrivers() {
        List<Driver> drivers = new ArrayList<>();
        String sql = "SELECT * FROM driver";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Driver driver = new Driver();
                driver.setDriverID(rs.getInt("DriverID"));
                driver.setEmpType(rs.getString("Emp_type"));
                driver.setName(rs.getString("Name"));
                driver.setDutyStatus(rs.getString("Duty_Status"));
                driver.setImageURL(rs.getString("ImageURL"));
                driver.setUsername(rs.getString("Username"));
                driver.setEmail(rs.getString("Email"));
                driver.setPhoneNumber(rs.getString("PhoneNumber"));
                driver.setPasswordHash(rs.getString("PasswordHash"));
                driver.setDateJoined(rs.getTimestamp("DateJoined"));
                drivers.add(driver);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drivers;
    }

    // Get driver by ID
    public Driver getDriverById(int driverID) {
        Driver driver = null;
        String sql = "SELECT * FROM driver WHERE DriverID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    driver = new Driver();
                    driver.setDriverID(rs.getInt("DriverID"));
                    driver.setEmpType(rs.getString("Emp_type"));
                    driver.setName(rs.getString("Name"));
                    driver.setDutyStatus(rs.getString("Duty_Status"));
                    driver.setImageURL(rs.getString("ImageURL"));
                    driver.setUsername(rs.getString("Username"));
                    driver.setEmail(rs.getString("Email"));
                    driver.setPhoneNumber(rs.getString("PhoneNumber"));
                    driver.setPasswordHash(rs.getString("PasswordHash"));
                    driver.setDateJoined(rs.getTimestamp("DateJoined"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public Driver getDriverByUsername(String Username) {
        Driver driver = null;
        String sql = "SELECT * FROM driver WHERE Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, Username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    driver = new Driver();
                    driver.setDriverID(rs.getInt("DriverID"));
                    driver.setEmpType(rs.getString("Emp_type"));
                    driver.setName(rs.getString("Name"));
                    driver.setDutyStatus(rs.getString("Duty_Status"));
                    driver.setImageURL(rs.getString("ImageURL"));
                    driver.setUsername(rs.getString("Username"));
                    driver.setEmail(rs.getString("Email"));
                    driver.setPhoneNumber(rs.getString("PhoneNumber"));
                    driver.setPasswordHash(rs.getString("PasswordHash"));
                    driver.setDateJoined(rs.getTimestamp("DateJoined"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }
    // Method to update booking status
    public boolean updateBookingStatus(int driverID, int bookingID, String status) {
        String sql = "UPDATE booking SET Status = ? WHERE BookingID = ? AND DriverID = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, bookingID);
            stmt.setInt(3, driverID);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public Driver getDriverByID(int driverID) {
        String sql = "SELECT * FROM driver WHERE DriverID = ?";
        Driver driver = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    driver = new Driver();
                    driver.setDriverID(rs.getInt("DriverID"));
                    driver.setName(rs.getString("Name"));
                    driver.setPhoneNumber(rs.getString("PhoneNumber"));
                    driver.setImageURL(rs.getString("ImageURL"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driver;
    }

    public int getOnDutyCount(){
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM driver WHERE Duty_Status = 'On Duty'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()){
                count = rs.getInt("total");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return count;
    }
}

