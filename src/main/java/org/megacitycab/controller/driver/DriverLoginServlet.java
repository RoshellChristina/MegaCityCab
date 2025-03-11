package org.megacitycab.controller.driver;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

// Make sure to import your service and utility classes
import org.megacitycab.model.Driver;
import org.megacitycab.service.driver.DriverService;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;

@WebServlet("/DriverLoginServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // 5 MB max file size
public class DriverLoginServlet extends HttpServlet {
    private DriverService driverService;

    @Override
    public void init() throws ServletException {
        driverService = new DriverService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            loginDriver(request, response);
        } else if ("register".equals(action)) {
            registerDriver(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    // Login processing
    private void loginDriver(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Retrieve driver record by username
        Driver driver = driverService.getDriverByUsername(username);

        // Verify credentials using BcryptUtil
        if (driver != null && BcryptUtil.verifyPassword(password, driver.getPasswordHash())) {
            HttpSession session = request.getSession(true); // Create session if none exists
            session.setAttribute("loggedInDriver", driver);
            session.setAttribute("driverID", driver.getDriverID());

            // Redirect to driver dashboard
            response.sendRedirect("driver/driver-dashboard.jsp");
        } else {
            // Set error message and forward back to login page
            request.setAttribute("loginError", "Invalid username or password.");
            request.getRequestDispatcher("driver/driver-login.jsp").forward(request, response);
        }
    }

    // Registration processing with additional password error handling
    // Registration processing with additional password error handling
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

        // Validate and hash the password using BCrypt if provided
        String password = request.getParameter("password");
        if (password == null || password.trim().isEmpty()) {
            request.setAttribute("registerError", "Password is required.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
            return;
        }
        if (password.length() < 4) {
            request.setAttribute("registerError", "Password must be at least 8 characters long.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
            return;
        }
        if (!password.matches(".*[A-Z].*")) {
            request.setAttribute("registerError", "Password must contain at least one uppercase letter.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
            return;
        }
        if (!password.matches(".*[a-z].*")) {
            request.setAttribute("registerError", "Password must contain at least one lowercase letter.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
            return;
        }
        if (!password.matches(".*\\d.*")) {
            request.setAttribute("registerError", "Password must contain at least one digit.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
            return;
        }
        driver.setPasswordHash(BcryptUtil.hashPassword(password));

        // Attempt to add the driver. Since email and username are unique keys,
        // a failure here likely means a duplicate key error.
        boolean added = driverService.addDriver(driver);
        if (added) {
            response.sendRedirect("driver/driver-login.jsp");
        } else {
            request.setAttribute("registerError", "Registration failed. The username or email may already be in use.");
            request.getRequestDispatcher("driver/driver-registration.jsp").forward(request, response);
        }
    }

}
