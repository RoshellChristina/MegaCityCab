package org.megacitycab.model;

import java.util.Date;

public class Driver {
    private int driverID;
    private String empType;        // Always "company_emp"
    private String name;
    private String dutyStatus;
    private String imageURL;       // Stored as Base64 string; if null, JSP shows default icon
    private String username;
    private String email;
    private String phoneNumber;
    private String passwordHash;
    private Date dateJoined;

    // This transient field holds the currently assigned vehicle ID (for UI pre-selection)
    private Integer assignedVehicleID;

    public Driver() {}

    public Driver(int driverID, String empType, String name, String dutyStatus, String imageURL,
                  String username, String email, String phoneNumber, String passwordHash, Date dateJoined) {
        this.driverID = driverID;
        this.empType = empType;
        this.name = name;
        this.dutyStatus = dutyStatus;
        this.imageURL = imageURL;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.dateJoined = dateJoined;
    }

    // Getters and setters
    public int getDriverID() {
        return driverID;
    }
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }
    public String getEmpType() {
        return empType;
    }
    public void setEmpType(String empType) {
        this.empType = empType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDutyStatus() {
        return dutyStatus;
    }
    public void setDutyStatus(String dutyStatus) {
        this.dutyStatus = dutyStatus;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public Date getDateJoined() {
        return dateJoined;
    }
    public void setDateJoined(Date dateJoined) {
        this.dateJoined = dateJoined;
    }
    public Integer getAssignedVehicleID() {
        return assignedVehicleID;
    }
    public void setAssignedVehicleID(Integer assignedVehicleID) {
        this.assignedVehicleID = assignedVehicleID;
    }
}

