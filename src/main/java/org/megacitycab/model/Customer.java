package org.megacitycab.model;

import java.sql.Timestamp;

public class Customer {
    private int userId;
    // Holds a Base64 encoded string of the image binary data.
    private String imageData;
    private String name;
    private String username;
    private String email;
    private String nic;
    private String phoneNumber;
    private String passwordHash;
    private String address;
    private Timestamp dateRegistered;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getImageData() {
        return imageData;
    }
    public void setImageData(String imageData) {
        this.imageData = imageData;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getNic() {
        return nic;
    }
    public void setNic(String nic) {
        this.nic = nic;
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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Timestamp getDateRegistered() {
        return dateRegistered;
    }
    public void setDateRegistered(Timestamp dateRegistered) {
        this.dateRegistered = dateRegistered;
    }


    public int getCustomerID() {
        return this.userId;
    }

}
