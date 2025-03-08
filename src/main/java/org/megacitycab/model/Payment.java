package org.megacitycab.model;

import java.sql.Timestamp;

public class Payment {
    private int paymentID;
    private int bookingID;
    private int userID;
    private double amount;
    private Timestamp paymentDate;
    private String paymentMethod; // 'Credit Card','Debit Card','Cash'
    private String status; // 'Pending','Completed','Failed'

    // Getters and setters
    public int getPaymentID() {
        return paymentID;
    }
    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }
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
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public Timestamp getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}


