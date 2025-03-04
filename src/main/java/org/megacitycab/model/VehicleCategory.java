package org.megacitycab.model;

import java.util.Base64;
import java.util.Date;

public class VehicleCategory {
    private int categoryID;
    private String categoryName;
    private byte[] imageData; // Store image as binary
    private Date createdDate;
    private double price;
    // Constructors
    public VehicleCategory() {}

    public VehicleCategory(int categoryID, String categoryName, byte[] imageData, Date createdDate, double price) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.imageData = imageData;
        this.createdDate = createdDate;
        this.price = price;
    }

    // Getters and Setters
    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Convert image bytes to Base64 string
    public String getBase64Image() {
        if (imageData != null) {
            return Base64.getEncoder().encodeToString(imageData);
        }
        return null;
    }
}
