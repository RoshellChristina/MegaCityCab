package org.megacitycab.model;

public class Vehicle {
    private int vehicleID;
    private int categoryID;
    private String licensePlate;
    private String color;
    private String status; // "Active" or "Inactive"
    private Integer driverID; // Can be null
    private VehicleCategory category; // Linked category object

    public Vehicle() {}

    public Vehicle(int vehicleID, int categoryID, String licensePlate, String color, String status, Integer driverID) {
        this.vehicleID = vehicleID;
        this.categoryID = categoryID;
        this.licensePlate = licensePlate;
        this.color = color;
        this.status = status;
        this.driverID = driverID;
    }

    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDriverID() {
        return driverID;
    }

    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }
}

