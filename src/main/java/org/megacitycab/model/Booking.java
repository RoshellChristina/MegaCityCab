package org.megacitycab.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingID;
    private int userID;
    private Integer driverID; // can be null initially
    private Integer vehicleID; // can be null initially
    private int vehicleCategoryID;
    private String pickupAddress;
    private String dropoffAddress;
    private Timestamp bookingDate;
    private double distanceKm;
    private Timestamp bookingMadeAt;
    private String status; // 'Pending','Accepted','In Progress','Completed','Cancelled'
private String userName;
    private Driver driver; // New field to hold Driver object
    private Vehicle vehicle; private Timestamp bookingEndTime;

    // ... existing getters and setters ...

    public Timestamp getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(Timestamp bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }


    // Getter and setter for Driver
    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    // Getter and setter for Vehicle
    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getters and setters
    public int getBookingID() {
        return bookingID;
    }
    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public Integer getDriverID() {
        return driverID;
    }
    public void setDriverID(Integer driverID) {
        this.driverID = driverID;
    }
    public Integer getVehicleID() {
        return vehicleID;
    }
    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }
    public int getVehicleCategoryID() {
        return vehicleCategoryID;
    }
    public void setVehicleCategoryID(int vehicleCategoryID) {
        this.vehicleCategoryID = vehicleCategoryID;
    }
    public String getPickupAddress() {
        return pickupAddress;
    }
    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }
    public String getDropoffAddress() {
        return dropoffAddress;
    }
    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }
    public Timestamp getBookingDate() {
        return bookingDate;
    }
    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }
    public double getDistanceKm() {
        return distanceKm;
    }
    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }
    public Timestamp getBookingMadeAt() {
        return bookingMadeAt;
    }
    public void setBookingMadeAt(Timestamp bookingMadeAt) {
        this.bookingMadeAt = bookingMadeAt;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}

