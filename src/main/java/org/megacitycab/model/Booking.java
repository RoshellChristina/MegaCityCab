package org.megacitycab.model;

import java.sql.Timestamp;

public class Booking {

    private int bookingID;
    private int userID;
    private Integer driverID; // Initially null (to be assigned later)
    private Integer vehicleID; // Initially null (to be assigned later)
    private String pickupAddress;
    private String dropoffAddress;
    private VehicleCategory category;
    private Timestamp bookingDate;
    private Timestamp bookingMadeAt;
    private String status;

    // Default constructor
    public Booking() {}

    // Constructor with parameters
    public Booking(int userID, int vehicleCategory, String pickupAddress, String dropoffAddress) {
        this.userID = userID;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;

        this.bookingDate = new Timestamp(System.currentTimeMillis());
        this.bookingMadeAt = new Timestamp(System.currentTimeMillis());
        this.status = "Pending";  // Default status
    }

    public Booking(int userID, int vehicleCategory, String pickupAddress, String dropoffAddress, String pickupDateTime) {
    }

    // Getters and setters
    public int getBookingID() { return bookingID; }
    public void setBookingID(int bookingID) { this.bookingID = bookingID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public Integer getDriverID() { return driverID; }
    public void setDriverID(Integer driverID) { this.driverID = driverID; }

    public Integer getVehicleID() { return vehicleID; }
    public void setVehicleID(Integer vehicleID) { this.vehicleID = vehicleID; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public String getDropoffAddress() { return dropoffAddress; }
    public void setDropoffAddress(String dropoffAddress) { this.dropoffAddress = dropoffAddress; }

    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }

    public Timestamp getBookingMadeAt() { return bookingMadeAt; }
    public void setBookingMadeAt(Timestamp bookingMadeAt) { this.bookingMadeAt = bookingMadeAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }


    public VehicleCategory getCategory() {
        return category;
    }

}
