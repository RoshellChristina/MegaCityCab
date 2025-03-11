package org.megacitycab.controller.driver;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.megacitycab.model.Driver;
import org.megacitycab.model.Vehicle;
import org.megacitycab.model.VehicleCategory;
import org.megacitycab.service.driver.DriverService;
import org.megacitycab.service.admin.VehicleService;
import org.megacitycab.service.admin.VehicleCategoryService;
import java.io.IOException;
import java.util.List;

@WebServlet("/DriverProfileServlet")
public class DriverProfileServlet extends HttpServlet {
    private DriverService driverService;
    private VehicleService vehicleService;
    private VehicleCategoryService vehicleCategoryService;

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
        vehicleService = new VehicleService();
        vehicleCategoryService = new VehicleCategoryService();
    }

    // Display profile page with current driver (and vehicle) info
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Driver driver = (Driver) request.getSession().getAttribute("loggedInDriver");
        if (driver == null) {
            response.sendRedirect(request.getContextPath() + "/driver/driver-login.jsp");
            return;
        }
        if ("freelance".equalsIgnoreCase(driver.getEmpType())) {
            // Load vehicle info if any
            Vehicle vehicle = vehicleService.getVehicleByDriverId(driver.getDriverID());
            request.setAttribute("driverVehicle", vehicle);
            // Load vehicle categories for the dropdown
            List<VehicleCategory> categories = vehicleCategoryService.getAllCategories();
            request.setAttribute("vehicleCategories", categories);
        }
        request.getRequestDispatcher("/driver/manage-profile.jsp").forward(request, response);
    }

    // Process profile updates and vehicle registrations/updates
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        Driver driver = (Driver) request.getSession().getAttribute("loggedInDriver");
        if (driver == null) {
            response.sendRedirect(request.getContextPath() + "/driver/driver-login.jsp");
            return;
        }
        if ("updateProfile".equals(action)) {
            // Update driver profile details
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String password = request.getParameter("password");

            driver.setName(name);
            driver.setEmail(email);
            driver.setPhoneNumber(phoneNumber);
            if (password != null && !password.trim().isEmpty()) {
                // Hash the password appropriately in a real app.
                driver.setPasswordHash(password);
            }
            if (driverService.updateDriver(driver)) {
                request.getSession().setAttribute("loggedInDriver", driver);
                request.getSession().setAttribute("message", "Profile updated successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?success=Profile updated");
            } else {

                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?error=Failed to update profile");
            }
        } else if ("registerVehicle".equals(action)) {
            // Register new vehicle for freelance driver
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String licensePlate = request.getParameter("licensePlate");
            String color = request.getParameter("color");
            Vehicle vehicle = new Vehicle();
            vehicle.setDriverID(driver.getDriverID());
            vehicle.setCategoryID(categoryID);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setColor(color);
            vehicle.setStatus("Active"); // Default status
            if (vehicleService.addVehicle(vehicle)) {
                // Save the new vehicle ID in driver (for UI pre-selection, etc.)
                driver.setAssignedVehicleID(vehicle.getVehicleID());
                request.getSession().setAttribute("loggedInDriver", driver);
                request.getSession().setAttribute("message", "Vehicle registered successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?success=Vehicle registered");
            } else {
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?error=Failed to register vehicle");
            }
        } else if ("updateVehicle".equals(action)) {
            // Update existing vehicle info
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String licensePlate = request.getParameter("licensePlate");
            String color = request.getParameter("color");
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleID(vehicleID);
            vehicle.setDriverID(driver.getDriverID());
            vehicle.setCategoryID(categoryID);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setColor(color);
            if (vehicleService.updateVehicle(vehicle)) {
                request.getSession().setAttribute("message", "Vehicle updated successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?success=Vehicle updated");
            } else {
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?error=Failed to update vehicle");
            }
        }else if ("deleteAccount".equals(action)) {
            // Delete driver account
            if (driverService.deleteDriver(driver.getDriverID())) {  // Assume deleteDriver() returns a boolean
                request.getSession().invalidate(); // Invalidate session after deletion
                response.sendRedirect(request.getContextPath() + "/driver/driver-login.jsp?success=Account deleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?error=Failed to delete account");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/DriverProfileServlet?error=Invalid action");
        }
    }
}

