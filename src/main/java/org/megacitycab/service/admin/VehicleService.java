package org.megacitycab.service.admin;

import org.megacitycab.dao.VehicleDAO;
import org.megacitycab.model.Vehicle;

import java.util.List;

public class VehicleService {
    private VehicleDAO vehicleDAO = new VehicleDAO();

    public boolean addVehicle(Vehicle vehicle) {
        return vehicleDAO.addVehicle(vehicle);
    }

    public boolean updateVehicle(Vehicle vehicle) {
        return vehicleDAO.updateVehicle(vehicle);
    }

    public boolean deleteVehicle(int vehicleID) {
        return vehicleDAO.deleteVehicle(vehicleID);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleDAO.getAllVehicles();
    }

    public List<Vehicle> getVehiclesByCategory(int categoryID) {
        return vehicleDAO.getVehiclesByCategory(categoryID);
    }

    public Vehicle getVehicleById(int vehicleID) {
        return vehicleDAO.getVehicleById(vehicleID);
    }

    public List<Vehicle> getInactiveVehicles() {
        return vehicleDAO.getInactiveVehicles();
    }

    public boolean assignVehicleToDriver(int vehicleID, int driverID) {
        return vehicleDAO.assignVehicleToDriver(vehicleID, driverID);
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleDAO.getInactiveVehicles();
    }

    public Vehicle getVehicleByDriverId(int driverID) {
        return vehicleDAO.getVehicleByDriverId(driverID);
    }


    public Vehicle getVehicleByID(int vehicleID) {
        return vehicleDAO.getVehicleByID(vehicleID);
    }
}

