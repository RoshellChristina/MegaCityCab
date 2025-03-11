package org.megacitycab.controller.customer;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.megacitycab.model.VehicleCategory;
import org.megacitycab.model.Review;
import org.megacitycab.service.admin.VehicleCategoryService;
import org.megacitycab.service.customer.ReviewService;

@WebServlet("/customer-home")
public class CustomerHomeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        VehicleCategoryService categoryService = new VehicleCategoryService();
        List<VehicleCategory> categories = categoryService.getAllCategories();


        ReviewService reviewService = new ReviewService();
        List<Review> reviews = reviewService.getAllReviews();

        // Set the data as request attributes
        request.setAttribute("categories", categories);
        request.setAttribute("reviews", reviews);


        RequestDispatcher dispatcher = request.getRequestDispatcher("/customer/customer-dashboard.jsp");
        dispatcher.forward(request, response);
    }
}
