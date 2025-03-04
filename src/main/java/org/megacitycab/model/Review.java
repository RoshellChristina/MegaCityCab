package org.megacitycab.model;

import java.util.Date;

public class Review {
    private int reviewID;
    private int userID;    // The customer who submitted the review
    private int driverID;
    private int rating;
    private String comments;
    private Date reviewDate;

    public Review() {}

    // Getters and Setters
    public int getReviewID() {
        return reviewID;
    }
    public void setReviewID(int reviewID) {
        this.reviewID = reviewID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getDriverID() {
        return driverID;
    }
    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public Date getReviewDate() {
        return reviewDate;
    }
    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }
}
