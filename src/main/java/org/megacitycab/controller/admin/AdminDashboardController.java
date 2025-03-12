package org.megacitycab.controller.admin;

import org.megacitycab.model.Booking;
import org.megacitycab.service.admin.AdminDashboardService;
import org.megacitycab.service.admin.analytics.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.megacitycab.service.customer.BookingService;


@WebServlet("/dashboard")
public class AdminDashboardController extends HttpServlet {
    private final AdminDashboardService service = new AdminDashboardService();
    private final BookingService bookingService = new BookingService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if the admin session exists
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp?error=notloggedin");
            return;
        }
        try {
            String month = req.getParameter("month");
            Double revenue = (month != null && !month.trim().isEmpty())
                    ? (Double) service.getAnalytics(new RevenueByMonthStrategy(month))
                    : (Double) service.getAnalytics(new TotalRevenueStrategy());

            req.setAttribute("selectedMonth", month);
            req.setAttribute("totalRevenue", revenue);

            // Fetch and set bookings
            List<Booking> bookings = bookingService.getBookings(month);
            System.out.println("Bookings found: " + bookings.size()); // Debug check
            req.setAttribute("bookings", bookings);

            int pendingBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Pending"));
            int acceptedBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Accepted"));
            int inProgressBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("In Progress"));
            int completedBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Completed"));
            int paidBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Paid"));
            int cancelledBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Cancelled"));
            int totalDrivers = (Integer) service.getAnalytics(new TotalDriverCountStrategy());
            int totalVehicles = (Integer) service.getAnalytics(new TotalVehicleCountStrategy());

            String mostDemandedCategory = (String) service.getAnalytics(new MostDemandedVehicleCategoryStrategy());

            // Set the analytics as request attributes
            req.setAttribute("totalRevenue", revenue);
            req.setAttribute("pendingBookings", pendingBookings);
            req.setAttribute("acceptedBookings", acceptedBookings);
            req.setAttribute("inProgressBookings", inProgressBookings);
            req.setAttribute("completedBookings", completedBookings);
            req.setAttribute("paidBookings", paidBookings);
            req.setAttribute("cancelledBookings", cancelledBookings);
            req.setAttribute("totalDrivers", totalDrivers);
            req.setAttribute("totalVehicles", totalVehicles);

            req.setAttribute("mostDemandedCategory", mostDemandedCategory);


            // Forward to the dashboard JSP page
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching dashboard analytics.", e);
        }
    }
}
