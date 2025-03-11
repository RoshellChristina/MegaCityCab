package org.megacitycab.controller.driver;

import org.megacitycab.model.Booking;
import org.megacitycab.model.Driver;
import org.megacitycab.service.driver.DriverService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/pending-bookings")
public class DriverBookingController extends HttpServlet {
    private DriverService driverService = new DriverService();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInDriver") != null) {
            Driver driver = (Driver) session.getAttribute("loggedInDriver");
            List<Booking> pendingBookings = driverService.getPendingBookingsForDriver(driver.getDriverID());
            request.setAttribute("pendingBookings", pendingBookings);
            request.getRequestDispatcher("/driver/pending-bookings.jsp").forward(request, response);  // Forward to pending bookings page
        } else {
            response.sendRedirect("driver/driver-login.jsp");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("loggedInDriver") != null) {
            Driver driver = (Driver) session.getAttribute("loggedInDriver");

            String bookingIDParam = request.getParameter("bookingID");
            if (bookingIDParam == null || bookingIDParam.isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/pending-bookings?error=invalid");
                return;
            }

            int bookingID = Integer.parseInt(bookingIDParam);
            boolean success = driverService.acceptBooking(driver.getDriverID(), bookingID);

            if (success) {
                // Redirect to the accepted bookings page
                request.getSession().setAttribute("message", "Booking accepted successfully!");
                response.sendRedirect(request.getContextPath() + "/pending-bookings?success=true");
            } else {
                // Stay on pending bookings page in case of failure
                response.sendRedirect(request.getContextPath() + "/driver/pending-bookings.jsp?error=true");
            }
        } else {
            response.sendRedirect("/driver/driver-login.jsp");
        }
    }
}




