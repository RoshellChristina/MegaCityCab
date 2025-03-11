package org.megacitycab.controller.driver;

import org.megacitycab.model.Driver;
import org.megacitycab.model.Review;
import org.megacitycab.service.customer.ReviewService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/DriverReviewController")
public class DriverReviewController extends HttpServlet {
    private ReviewService reviewService;

    @Override
    public void init() {
        reviewService = new ReviewService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Driver loggedInDriver = (Driver) session.getAttribute("loggedInDriver");

        if (loggedInDriver == null) {
            response.sendRedirect("driver/driver-login.jsp");
            return;
        }

        int driverID = loggedInDriver.getDriverID();
        List<Review> reviewList = reviewService.getReviewsByDriverID(driverID);

        request.setAttribute("reviewList", reviewList);
        request.getRequestDispatcher("driver/reviews.jsp").forward(request, response);
    }
}
