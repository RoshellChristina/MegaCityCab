package org.megacitycab.controller.admin;

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
import java.util.Map;

@WebServlet("/dashboard")
public class AdminDashboardController extends HttpServlet {
    private final AdminDashboardService service = new AdminDashboardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Check if the admin session exists
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/login.jsp?error=notloggedin");
            return;
        }
        try {
            // Calculate analytics using strategy implementations
            Double totalRevenue = (Double) service.getAnalytics(new TotalRevenueStrategy());
            int pendingBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Pending"));
            int acceptedBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Accepted"));
            int inProgressBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("In Progress"));
            int completedBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Completed"));
            int cancelledBookings = (Integer) service.getAnalytics(new BookingStatusCountStrategy("Cancelled"));
            int totalDrivers = (Integer) service.getAnalytics(new TotalDriverCountStrategy());
            int totalVehicles = (Integer) service.getAnalytics(new TotalVehicleCountStrategy());
            Map<String, Double> monthlyRevenue = (Map<String, Double>) service.getAnalytics(new MonthlyRevenueStrategy());
            String mostDemandedCategory = (String) service.getAnalytics(new MostDemandedVehicleCategoryStrategy());

            // Format monthly revenue data
            StringBuilder months = new StringBuilder();
            StringBuilder revenue = new StringBuilder();
            for (Map.Entry<String, Double> entry : monthlyRevenue.entrySet()) {
                months.append("'").append(entry.getKey()).append("',");
                revenue.append(entry.getValue()).append(",");
            }
            if (months.length() > 0) months.setLength(months.length() - 1);
            if (revenue.length() > 0) revenue.setLength(revenue.length() - 1);

            // Pass formatted data to the JSP
            req.setAttribute("monthlyRevenueMonths", months.toString());
            req.setAttribute("monthlyRevenue", revenue.toString());

            // Set the analytics as request attributes
            req.setAttribute("totalRevenue", totalRevenue);
            req.setAttribute("pendingBookings", pendingBookings);
            req.setAttribute("acceptedBookings", acceptedBookings);
            req.setAttribute("inProgressBookings", inProgressBookings);
            req.setAttribute("completedBookings", completedBookings);
            req.setAttribute("cancelledBookings", cancelledBookings);
            req.setAttribute("totalDrivers", totalDrivers);
            req.setAttribute("totalVehicles", totalVehicles);
            req.setAttribute("monthlyRevenue", monthlyRevenue);
            req.setAttribute("mostDemandedCategory", mostDemandedCategory);

            // Forward to the dashboard JSP page
            req.getRequestDispatcher("/admin/dashboard.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException("Database error while fetching dashboard analytics.", e);
        }
    }
}
