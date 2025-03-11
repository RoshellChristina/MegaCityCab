package org.megacitycab.controller.driver;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Driver;
import org.megacitycab.service.driver.DriverService;

import java.io.IOException;
import java.util.List;

@WebServlet("/accepted-bookings")


    public class AcceptedBookingsController extends HttpServlet {
        private DriverService driverService = new DriverService();

        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            HttpSession session = request.getSession(false);
            if (session != null && session.getAttribute("loggedInDriver") != null) {
                Driver driver = (Driver) session.getAttribute("loggedInDriver");
                List<Booking> acceptedBookings = driverService.getAcceptedBookingsForDriver(driver.getDriverID());
                request.setAttribute("acceptedBookings", acceptedBookings);
                request.getRequestDispatcher("driver/accepted-bookings.jsp").forward(request, response);
            } else {
                response.sendRedirect("driver/driver-login.jsp");
            }
        }
    }

