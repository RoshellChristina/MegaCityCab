package org.megacitycab.controller.driver;

import org.megacitycab.service.driver.DriverService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/update-booking-status")
public class UpdateBookingStatusController extends HttpServlet {
    private DriverService driverService = new DriverService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Ensure the driver is logged in
        if (session != null && session.getAttribute("loggedInDriver") != null) {
            Integer driverID = (Integer) session.getAttribute("driverID");
            String bookingIDParam = request.getParameter("bookingID");
            String status = request.getParameter("status");

            if (bookingIDParam != null && !bookingIDParam.isEmpty() && status != null) {
                int bookingID = Integer.parseInt(bookingIDParam);

                // Update the booking status in the database using DriverService
                boolean success = driverService.updateBookingStatus(driverID, bookingID, status);

                if (success) {
                    request.getSession().setAttribute("message", "Status updated successfully!");
                    response.sendRedirect(request.getContextPath() + "/accepted-bookings?success=true");
                } else {
                    request.getSession().setAttribute("message", "Status update failed!");
                    response.sendRedirect(request.getContextPath() + "/accepted-bookings?error=true");
                }
            } else {

                response.sendRedirect(request.getContextPath() + "/accepted-bookings?error=invalid");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/driver/driver-login.jsp");
        }
    }
}
