package org.megacitycab.service.customer;

import org.megacitycab.dao.ReviewDAO;
import org.megacitycab.model.Review;
import java.util.List;
import java.util.Date;

public class ReviewService {
    private ReviewDAO reviewDAO = new ReviewDAO();

    // Add review and return success status
    public boolean addReview(Review review) {
        if (review.getReviewDate() == null) {
            review.setReviewDate(new Date());
        }
        try {
            return reviewDAO.addReview(review);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update review
    public boolean updateReview(Review review) {
        try {
            return reviewDAO.updateReview(review);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete review by ID
    public boolean deleteReview(int reviewID) {
        try {
            return reviewDAO.deleteReview(reviewID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a specific review by ID
    public Review getReviewById(int reviewID) {
        try {
            return reviewDAO.getReviewById(reviewID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Get all reviews by a specific user ID
    public List<Review> getReviewsByUserId(int userID) {
        try {
            return reviewDAO.getReviewsByUserId(userID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Review> getReviewsByDriverID(int driverID) {
        return reviewDAO.getReviewsByDriverID(driverID);
    }
}

