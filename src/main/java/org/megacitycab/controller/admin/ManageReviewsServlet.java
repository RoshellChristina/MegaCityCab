package org.megacitycab.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.megacitycab.model.Review;
import org.megacitycab.service.customer.ReviewService;

import java.io.IOException;
import java.util.List;

@WebServlet("/ViewAllReviewsServlet")
public class ManageReviewsServlet extends HttpServlet {
    private final ReviewService reviewService = new ReviewService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ratingFilter = request.getParameter("rating");
        List<Review> reviews;

        // Check if the rating filter is null or empty (for "All Ratings")
        if (ratingFilter == null || ratingFilter.isEmpty()) {
            reviews = reviewService.getAllReviews();
        } else {
            try {
                int rating = Integer.parseInt(ratingFilter);
                reviews = reviewService.getReviewsByRating(rating);
            } catch (NumberFormatException e) {
                // Fallback to all reviews if parsing fails
                reviews = reviewService.getAllReviews();
            }
        }

        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("admin/manage-reviews.jsp").forward(request, response);
    }
}
