package org.megacitycab.controller.customer;

import org.megacitycab.model.Review;
import org.megacitycab.service.customer.ReviewService;
import org.megacitycab.service.driver.DriverService; // Assumed to exist
import org.megacitycab.model.Driver;         // Assumed to exist

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet("/ReviewController")
public class ReviewController extends HttpServlet {
    private ReviewService reviewService;
    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService();
        driverService = new DriverService();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || "list".equals(action)) {
            listReviews(request, response);
        } else if ("edit".equals(action)) {
            editReview(request, response);
        } else if ("delete".equals(action)) {
            deleteReview(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addReview(request, response);
        } else if ("update".equals(action)) {
            updateReview(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void addReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get customer ID from session
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        try {
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comments = request.getParameter("comments");

            Review review = new Review();
            review.setUserID(customerID);
            review.setDriverID(driverID);
            review.setRating(rating);
            review.setComments(comments);

            if (reviewService.addReview(review)) {
                response.sendRedirect("ReviewController?action=list&success=Review added");
            } else {
                response.sendRedirect("customer/manage-reviews.jsp?error=Failed to add review");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customer/manage-reviews.jsp?error=Invalid input");
        }
    }

    private void updateReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comments = request.getParameter("comments");

            Review review = new Review();
            review.setReviewID(reviewID);
            review.setUserID(customerID);
            review.setDriverID(driverID);
            review.setRating(rating);
            review.setComments(comments);
            // Set the review date for the update (this was missing)
            review.setReviewDate(new Date());

            if (reviewService.updateReview(review)) {
                response.sendRedirect("ReviewController?action=list&success=Review updated");
            } else {
                response.sendRedirect("customer/manage-reviews.jsp?error=Failed to update review");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customer/manage-reviews.jsp?error=Invalid input");
        }
    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            if (reviewService.deleteReview(reviewID)) {
                response.sendRedirect("ReviewController?action=list&success=Review deleted");
            } else {
                response.sendRedirect("customer/manage-reviews.jsp?error=Failed to delete review");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customer/manage-reviews.jsp?error=Invalid request");
        }
    }

    private void listReviews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get customer ID from session
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        // Fetch reviews by the logged-in user
        List<Review> reviewList = reviewService.getReviewsByUserId(customerID);
        request.setAttribute("reviewList", reviewList);

        // Fetch driver list for the dropdown
        List<Driver> driverList = driverService.getAllDrivers();
        request.setAttribute("driverList", driverList);

        request.getRequestDispatcher("customer/manage-reviews.jsp").forward(request, response);
    }

    private void editReview(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int reviewID = Integer.parseInt(request.getParameter("reviewID"));
            Review review = reviewService.getReviewById(reviewID);
            request.setAttribute("review", review);

            // Load driver list as well
            List<Driver> driverList = driverService.getAllDrivers();
            request.setAttribute("driverList", driverList);

            request.getRequestDispatcher("customer/manage-reviews.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("customer/manage-reviews.jsp?error=Review not found");
        }
    }
}


