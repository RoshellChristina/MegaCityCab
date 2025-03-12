package org.megacitycab.controller.customer;

import jakarta.servlet.annotation.WebServlet;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Driver;
import org.megacitycab.model.Vehicle;
import org.megacitycab.model.VehicleCategory;
import org.megacitycab.service.admin.VehicleCategoryService;
import org.megacitycab.service.admin.VehicleService;
import org.megacitycab.service.customer.BookingService;
import org.megacitycab.service.customer.strategy.FareCalculationStrategy;
import org.megacitycab.service.customer.strategy.FareStrategySelector;
import org.megacitycab.service.customer.PaymentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.megacitycab.service.driver.DriverService;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

@WebServlet("/customerbooking")
public class CustomerBookingController extends HttpServlet {

    private BookingService bookingService = new BookingService();
    private PaymentService paymentService = new PaymentService();
    private VehicleCategoryService vehicleCategoryService = new VehicleCategoryService();
    private DriverService driverService = new DriverService();
    private VehicleService vehicleService = new VehicleService();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        if ("view".equals(action)) {
            // Retrieve and display the customer’s bookings
            List<Booking> bookings = bookingService.getBookingsByUserId(customerID);
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("customer/bookings.jsp").forward(request, response);
        } else if ("cancel".equals(action)) {
            // Cancel a booking
            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            boolean cancelled = bookingService.cancelBooking(bookingID);
            if (cancelled) {
                boolean paymentUpdated = paymentService.updatePaymentStatus(bookingID, "Cancelled");
                if (paymentUpdated) {
                    request.setAttribute("message", "Booking and payment cancelled successfully.");
                } else {
                    request.setAttribute("message", "Booking cancelled.");
                }
            } else {
                request.setAttribute("message", "Failed to cancel booking.");
            }
            List<Booking> bookings = bookingService.getBookingsByUserId(customerID);
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("customer/bookings.jsp").forward(request, response);
        }else if ("viewDetails".equals(action)) {
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            int bookingID = Integer.parseInt(request.getParameter("bookingID"));
            Booking booking = bookingService.getBookingDetailsWithDriverAndVehicle(bookingID);


            // Debug: show booking ID and retrieved IDs
            out.write("<p>Booking ID: " + bookingID + "</p>");

            if (booking != null) {
                out.write("<p>DriverID: " + booking.getDriverID() + "</p>");
                out.write("<p>VehicleID: " + booking.getVehicleID() + "</p>");

                if (booking.getDriverID() > 0 && booking.getVehicleID() > 0) {
                    Driver driver = driverService.getDriverByID(booking.getDriverID());
                    Vehicle vehicle = vehicleService.getVehicleByID(booking.getVehicleID());

                    if (driver != null && vehicle != null) {
                        out.write("<p><b>Driver Name:</b> " + driver.getName() + "</p>");
                        out.write("<p><b>Driver Phone:</b> " + driver.getPhoneNumber() + "</p>");
                        out.write("<p><b>Vehicle License Plate:</b> " + vehicle.getLicensePlate() + "</p>");
                        out.write("<p><b>Vehicle Color:</b> " + vehicle.getColor() + "</p>");
                    } else {
                        out.write("<p>Error: Driver or Vehicle details not found.</p>");
                    }
                } else {
                    out.write("<p>Error: Booking details incomplete (missing driver or vehicle ID).</p>");
                }
            } else {
                out.write("<p>Error: Booking record not found.</p>");
            }

            out.flush();
        } else if ("calculateFare".equals(action)) {
            try {
                int vehicleCategoryID = Integer.parseInt(request.getParameter("vehicleCategoryID"));
                String bookingDateStr = request.getParameter("bookingDate");
                // Convert bookingDate from "yyyy-MM-ddTHH:mm" to Timestamp
                bookingDateStr = bookingDateStr.replace("T", " ") + ":00";
                Timestamp bookingTimestamp = Timestamp.valueOf(bookingDateStr);
                double distanceKm = Double.parseDouble(request.getParameter("distanceKm"));

                // Get the category and its base price
                VehicleCategory category = vehicleCategoryService.getCategoryById(vehicleCategoryID);
                double basePrice = category.getPrice();

                // Select the fare strategy and calculate fare
                FareCalculationStrategy fareStrategy = FareStrategySelector.getFareStrategy(bookingTimestamp);
                double fare = fareStrategy.calculateFare(basePrice, distanceKm);

                // Return the fare as JSON
                response.setContentType("application/json");
                response.getWriter().write("{\"fare\":" + fare + "}");
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"error\":\"Error calculating fare.\"}");
            }
            return;
        }
        else {
            // Default action: display the booking form with available vehicle categories
            List<VehicleCategory> categories = vehicleCategoryService.getAllCategories();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("customer/booking-form.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        // Check if this request is for processing a payment.
        String action = request.getParameter("action");
        if ("pay".equals(action)) {
            try {
                int bookingID = Integer.parseInt(request.getParameter("bookingID"));
                // Retrieve the booking to recalculate fare
                // Retrieve the booking to recalculate fare
                Booking booking = bookingService.getBookingById(bookingID);
                if (booking != null) {
                    int vehicleCategoryID = booking.getVehicleCategoryID();
                    double distanceKm = booking.getDistanceKm();
                    // Get the category details
                    VehicleCategory category = vehicleCategoryService.getCategoryById(vehicleCategoryID);
                    double basePrice = category.getPrice();

                    // Select the appropriate fare strategy based on booking time
                    FareCalculationStrategy fareStrategy = FareStrategySelector.getFareStrategy(booking.getBookingDate());

                    // Calculate fare using the selected strategy
                    double amount = fareStrategy.calculateFare(basePrice, distanceKm);

                    String paymentMethod = request.getParameter("paymentMethod");
                    boolean paymentSaved = paymentService.createPayment(bookingID, customerID, amount, paymentMethod);
                    if (paymentSaved) {
                        bookingService.markBookingPaid(bookingID);
                        request.setAttribute("downloadReceipt", bookingID);
                        request.setAttribute("message", "Payment recorded successfully.");
                    } else {
                        request.setAttribute("message", "Payment already made.");
                    }
                } else {
                    request.setAttribute("message", "Invalid booking for payment.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("message", "Error processing payment: " + e.getMessage());
            }
            List<Booking> bookings = bookingService.getBookingsByUserId(customerID);
            request.setAttribute("bookings", bookings);
            request.getRequestDispatcher("customer/bookings.jsp").forward(request, response);
            return;
        }

        // Otherwise, handle booking creation (without processing payment)
        try {

            int vehicleCategoryID = Integer.parseInt(request.getParameter("vehicleCategoryID"));
            String pickupAddress = request.getParameter("pickupAddress");
            String dropoffAddress = request.getParameter("dropoffAddress");

            String bookingDateStr = request.getParameter("bookingDate");

            Timestamp bookingTimestamp = null;
            if (bookingDateStr != null && !bookingDateStr.isEmpty()) {
                try {
                    bookingDateStr = bookingDateStr.replace("T", " ") + ":00"; // Convert "yyyy-MM-ddTHH:mm" to "yyyy-MM-dd HH:mm:ss"
                    bookingTimestamp = Timestamp.valueOf(bookingDateStr);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace(); // Log error for debugging
                }
            }

            double distanceKm = Double.parseDouble(request.getParameter("distanceKm"));


            // Create the booking record
            int bookingID = bookingService.createBooking(customerID, vehicleCategoryID, pickupAddress, dropoffAddress, bookingTimestamp, distanceKm);

            if (bookingID > 0) {
                // No payment recorded now—the user will pay after ride completion.
                request.setAttribute("message", "Booking created successfully. Payment will be processed after ride completion.");
            } else if (bookingID == -2) {
                request.setAttribute("message", "Booking Failed! must be made at least 30 minutes in advance.");}
            else {
                request.setAttribute("message", "Failed to create booking.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "Error processing booking: " + e.getMessage());
        }
        List<Booking> bookings = bookingService.getBookingsByUserId(customerID);
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("customer/bookings.jsp").forward(request, response);
    }

}



