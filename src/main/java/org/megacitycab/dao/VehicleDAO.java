package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Vehicle;
import org.megacitycab.model.VehicleCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {
    private VehicleCategoryDAO categoryDAO = new VehicleCategoryDAO();

    // Add a new vehicle
    public boolean addVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicle (CategoryID, LicensePlate, Color, Status, DriverID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicle.getCategoryID());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getColor());
            stmt.setString(4, vehicle.getStatus());
            if (vehicle.getDriverID() == null) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, vehicle.getDriverID());
            }

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update vehicle
    public boolean updateVehicle(Vehicle vehicle) {
        String sql = "UPDATE vehicle SET CategoryID = ?, LicensePlate = ?, Color = ?, Status = ?, DriverID = ? WHERE VehicleID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicle.getCategoryID());
            stmt.setString(2, vehicle.getLicensePlate());
            stmt.setString(3, vehicle.getColor());
            stmt.setString(4, vehicle.getStatus());
            if (vehicle.getDriverID() == null) {
                stmt.setNull(5, Types.INTEGER);
            } else {
                stmt.setInt(5, vehicle.getDriverID());
            }
            stmt.setInt(6, vehicle.getVehicleID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete vehicle
    public boolean deleteVehicle(int vehicleID) {
        String sql = "DELETE FROM vehicle WHERE VehicleID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, vehicleID);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all vehicles
    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle();
                vehicle.setVehicleID(rs.getInt("VehicleID"));
                vehicle.setCategoryID(rs.getInt("CategoryID"));
                vehicle.setLicensePlate(rs.getString("LicensePlate"));
                vehicle.setColor(rs.getString("Color"));
                vehicle.setStatus(rs.getString("Status"));
                vehicle.setDriverID(rs.getInt("DriverID"));

                // Fetch category from categoryDAO
                VehicleCategory category = categoryDAO.getCategoryById(vehicle.getCategoryID());
                vehicle.setCategory(category);

                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Get vehicles by category
    public List<Vehicle> getVehiclesByCategory(int categoryID) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicle WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID(rs.getInt("VehicleID"));
                    vehicle.setCategoryID(rs.getInt("CategoryID"));
                    vehicle.setLicensePlate(rs.getString("LicensePlate"));
                    vehicle.setColor(rs.getString("Color"));
                    vehicle.setStatus(rs.getString("Status"));
                    vehicle.setDriverID(rs.getInt("DriverID"));

                    // Fetch category
                    VehicleCategory category = categoryDAO.getCategoryById(vehicle.getCategoryID());
                    vehicle.setCategory(category);

                    vehicles.add(vehicle);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleID) {
        // A simple (inefficient) implementation:
        List<Vehicle> vehicles = getAllVehicles();
        for (Vehicle v : vehicles) {
            if (v.getVehicleID() == vehicleID) {
                return v;
            }
        }
        return null;
    }

    // Get only inactive vehicles (available for assignment)
    public List<Vehicle> getInactiveVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT VehicleID, CategoryID, LicensePlate, Color FROM vehicle WHERE Status = 'Inactive'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("VehicleID"),
                        rs.getInt("CategoryID"),
                        rs.getString("LicensePlate"),
                        rs.getString("Color"),
                        "Inactive",
                        null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    // Assign a vehicle to a driver: update the vehicle's DriverID and mark it Active.
    public boolean assignVehicleToDriver(int vehicleID, int driverID) {
        String sql = "UPDATE vehicle SET DriverID = ?, Status = 'Active' WHERE VehicleID = ? AND Status = 'Inactive'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            stmt.setInt(2, vehicleID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // (Optional) Get vehicle by driver ID so you can pre-select an assigned vehicle in the UI.
    public Vehicle getVehicleByDriverId(int driverID) {
        String sql = "SELECT * FROM vehicle WHERE DriverID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, driverID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Vehicle vehicle = new Vehicle();
                    vehicle.setVehicleID(rs.getInt("VehicleID"));
                    vehicle.setCategoryID(rs.getInt("CategoryID"));
                    vehicle.setLicensePlate(rs.getString("LicensePlate"));
                    vehicle.setColor(rs.getString("Color"));
                    vehicle.setStatus(rs.getString("Status"));
                    vehicle.setDriverID(rs.getInt("DriverID"));
                    return vehicle;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public Vehicle getVehicleByID(int vehicleID) {
        String sql = "SELECT * FROM vehicle WHERE VehicleID = ?";
        Vehicle vehicle = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    vehicle = new Vehicle();
                    vehicle.setVehicleID(rs.getInt("VehicleID"));
                    vehicle.setLicensePlate(rs.getString("LicensePlate"));
                    vehicle.setColor(rs.getString("Color"));
                    vehicle.setStatus(rs.getString("Status"));
                    vehicle.setCategoryID(rs.getInt("CategoryID"));
                    vehicle.setDriverID(rs.getInt("DriverID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicle;
    }

}

