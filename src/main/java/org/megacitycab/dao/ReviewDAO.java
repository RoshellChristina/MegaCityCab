package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // Add review to the database
    public boolean addReview(Review review) {
        String sql = "INSERT INTO review (UserID, DriverID, Rating, Comments, ReviewDate) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))  {
            // Set parameters in the correct order
            stmt.setInt(1, review.getUserID());
            stmt.setInt(2, review.getDriverID());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComments());
            stmt.setTimestamp(5, new java.sql.Timestamp(review.getReviewDate().getTime()));
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Use proper logging in production
            return false;
        }
    }

    // Update an existing review in the database
    public boolean updateReview(Review review) {
        String sql = "UPDATE review SET DriverID=?, Rating=?, Comments=?, ReviewDate=? WHERE ReviewID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, review.getDriverID());
            stmt.setInt(2, review.getRating());
            stmt.setString(3, review.getComments());
            stmt.setTimestamp(4, new java.sql.Timestamp(review.getReviewDate().getTime()));
            stmt.setInt(5, review.getReviewID());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete a review from the database
    public boolean deleteReview(int reviewID) {
        String sql = "DELETE FROM review WHERE ReviewID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewID);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Review getReviewById(int reviewID) {
        Review review = null;
        String sql = "SELECT * FROM review WHERE ReviewID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, reviewID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    review = new Review();
                    review.setReviewID(rs.getInt("ReviewID"));
                    review.setUserID(rs.getInt("UserID"));
                    review.setDriverID(rs.getInt("DriverID"));
                    review.setRating(rs.getInt("Rating"));
                    review.setComments(rs.getString("Comments"));
                    review.setReviewDate(rs.getTimestamp("ReviewDate"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return review;
    }

    public List<Review> getReviewsByUserId(int userID) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE UserID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewID(rs.getInt("ReviewID"));
                    review.setUserID(rs.getInt("UserID"));
                    review.setDriverID(rs.getInt("DriverID"));
                    review.setRating(rs.getInt("Rating"));
                    review.setComments(rs.getString("Comments"));
                    review.setReviewDate(rs.getTimestamp("ReviewDate"));
                    reviews.add(review);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }


    public List<Review> getReviewsByDriverID(int driverID) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.ReviewID, r.UserID, r.DriverID, r.Rating, r.Comments, r.ReviewDate, u.Name " +
                "FROM review r " +
                "JOIN user u ON r.UserID = u.UserID " +  // Joining with user table to get FullName
                "WHERE r.DriverID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, driverID);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setReviewID(rs.getInt("ReviewID"));
                review.setUserID(rs.getInt("UserID"));
                review.setDriverID(rs.getInt("DriverID"));
                review.setRating(rs.getInt("Rating"));
                review.setComments(rs.getString("Comments"));
                review.setReviewDate(rs.getTimestamp("ReviewDate"));
                review.setUserName(rs.getString("Name"));

                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }
}
