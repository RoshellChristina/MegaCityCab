package org.megacitycab.controller.driver;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.megacitycab.model.Customer;
import org.megacitycab.model.Driver;
import org.megacitycab.model.Vehicle;
import org.megacitycab.service.driver.DriverService;
import org.megacitycab.service.admin.VehicleService;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.List;

@WebServlet("/DriverServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class DriverController extends HttpServlet {
    private DriverService driverService;
    private VehicleService vehicleService;

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
        vehicleService = new VehicleService();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("insert".equals(action)) {
            insertDriver(request, response);
        }else if("register".equals(action)){
            registerDriver(request, response);
        }

        else if ("edit".equals(action)) {
            updateDriver(request, response);
        } else if ("delete".equals(action)) {
            deleteDriver(request, response);
        }else if ("assign".equals(action)) {
            assignVehicle(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || "list".equals(action)) {
            // This method should handle setting attributes and forwarding
            listDrivers(request, response);
            return;
        } else if ("fetchAvailableVehicles".equals(action)) {
            List<Vehicle> availableVehicles = vehicleService.getInactiveVehicles(); // or getAvailableVehicles()
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("[");
            for (int i = 0; i < availableVehicles.size(); i++) {
                Vehicle v = availableVehicles.get(i);
                out.print("{\"vehicleID\": " + v.getVehicleID() +
                        ", \"licensePlate\": \"" + v.getLicensePlate() +
                        "\", \"color\": \"" + v.getColor() + "\"}");
                if (i < availableVehicles.size() - 1) {
                    out.print(",");
                }
            }
            out.print("]");
            out.flush();
            return;
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

    }

    private void insertDriver(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            Driver driver = new Driver();

            // Set employee type to "company_emp" by default
            driver.setEmpType("company_emp");

            // Handle file upload for image
            Part imagePart = request.getPart("imageFile");
            String imageURL;
            if (imagePart != null && imagePart.getSize() > 0) {
                try (InputStream is = imagePart.getInputStream()) {
                    imageURL = ImageUtil.convertToBase64(is);
                }
            } else {
                imageURL = ImageUtil.getDefaultImage();
            }
            driver.setImageURL(imageURL);

            // Populate driver fields from the request
            driver.setName(request.getParameter("name"));
            driver.setUsername(request.getParameter("username"));
            driver.setEmail(request.getParameter("email"));
            driver.setPhoneNumber(request.getParameter("phoneNumber"));
            // Set default duty status; adjust if you wish to retrieve from the form
            driver.setDutyStatus(request.getParameter("dutyStatus"));


            // Hash the password using BCrypt if provided
            String password = request.getParameter("password");
            if (password != null && !password.trim().isEmpty()) {
                driver.setPasswordHash(BcryptUtil.hashPassword(password));
            }

            // Call the service to add the driver and redirect based on the result
            if (driverService.addDriver(driver)) {
                request.getSession().setAttribute("message", "Driver added successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&success=Driver added");
            } else {
                request.getSession().setAttribute("message", "Driver already exists!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Failed to add driver");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Invalid input");
        }
    }


    private void registerDriver(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Driver driver = new Driver();

        // Set employee type to "freelance" for registration
        driver.setEmpType("freelance");

        // Handle file upload for image
        Part imagePart = request.getPart("imageFile");
        String imageURL;
        if (imagePart != null && imagePart.getSize() > 0) {
            try (InputStream is = imagePart.getInputStream()) {
                imageURL = ImageUtil.convertToBase64(is);
            }
        } else {
            imageURL = ImageUtil.getDefaultImage();
        }
        driver.setImageURL(imageURL);

        driver.setName(request.getParameter("name"));
        driver.setUsername(request.getParameter("username"));
        driver.setEmail(request.getParameter("email"));
        driver.setPhoneNumber(request.getParameter("phoneNumber"));
        // Set default duty status – adjust the value as needed (e.g., "Off Duty")
        driver.setDutyStatus("On Duty");

        // Hash the password using BCrypt if provided
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            driver.setPasswordHash(BcryptUtil.hashPassword(password));
        }

        driverService.addDriver(driver);
        response.sendRedirect("driver/driver-login.jsp");
    }


    private void updateDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            String name = request.getParameter("name");
            String dutyStatus = request.getParameter("dutyStatus");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String newPassword = request.getParameter("passwordHash"); // New password input

            // Retrieve the existing driver details
            Driver existingDriver = driverService.getDriverByID(driverID);
            if (existingDriver == null) {
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Driver not found");
                return;
            }

            // Use existing password if the field is empty
            String passwordHash = (newPassword == null || newPassword.isEmpty())
                    ? existingDriver.getPasswordHash()
                    : BcryptUtil.hashPassword(newPassword);

            // Process image if provided
            Part imagePart = request.getPart("image");
            String imageBase64 = null;
            if (imagePart != null && imagePart.getSize() > 0) {
                byte[] imageBytes = imagePart.getInputStream().readAllBytes();
                imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            }

            Driver driver = new Driver();
            driver.setDriverID(driverID);
            driver.setName(name);
            driver.setDutyStatus(dutyStatus);
            driver.setUsername(username);
            driver.setEmail(email);
            driver.setPhoneNumber(phoneNumber);
            driver.setPasswordHash(passwordHash);

            if (imageBase64 != null) {
                driver.setImageURL(imageBase64);
            } else {
                driver.setImageURL(existingDriver.getImageURL()); // Keep existing image if none is uploaded
            }

            if (driverService.updateDriver(driver)) {
                request.getSession().setAttribute("message", "Driver updated successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&success=Driver updated");
            } else {
                request.getSession().setAttribute("message", "Driver already exists!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Failed to update driver");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Invalid input");
        }
    }


    private void deleteDriver(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            if (driverService.deleteDriver(driverID)) {
                request.getSession().setAttribute("message", "Driver deleted successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&success=Driver deleted");
            } else {

                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Failed to delete driver");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Invalid request");
        }
    }

    private void listDrivers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Driver> drivers = driverService.getAllDrivers();
        // Optionally, for each driver, fetch assigned vehicle (if needed)
        for (Driver d : drivers) {
            Vehicle assigned = vehicleService.getVehicleByDriverId(d.getDriverID());
            if (assigned != null) {
                d.setAssignedVehicleID(assigned.getVehicleID());
                // Optionally, set additional vehicle details if you want to display more than just the ID.
            } else {
                d.setAssignedVehicleID(null);
            }
        }
        // Get available vehicles (only those inactive and unassigned)
        List<Vehicle> availableVehicles = vehicleService.getInactiveVehicles();
        request.setAttribute("drivers", drivers);
        request.setAttribute("availableVehicles", availableVehicles);
        request.getRequestDispatcher("/admin/manage-drivers.jsp").forward(request, response);
    }

    private void assignVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            if(vehicleService.assignVehicleToDriver(vehicleID, driverID)) {
                request.getSession().setAttribute("message", "Vehicle assigned successfully!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&success=Vehicle assigned");
            } else {
                request.getSession().setAttribute("message", "Vehicle already assigned!");
                response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Failed to assign vehicle");
            }
        } catch(Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DriverServlet?action=list&error=Invalid input");
        }
    }
}



