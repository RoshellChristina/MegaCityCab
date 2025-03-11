package org.megacitycab.controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.megacitycab.model.Customer;
import org.megacitycab.service.customer.CustomerService;
import org.megacitycab.service.customer.CustomerServiceImpl;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;
import org.megacitycab.observer.BookingNotificationManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/login")
public class CustomerLoginServlet extends HttpServlet {
    private CustomerService customerService = new CustomerServiceImpl();


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("login".equals(action)) {
            loginCustomer(request, response);
        } else if ("register".equals(action)) {
            registerCustomer(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void loginCustomer(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Fetch the customer from the database by username
        Customer customer = customerService.getCustomerByUsername(username);

        // Check if customer exists and if the password is correct
        if (customer != null && BcryptUtil.verifyPassword(password, customer.getPasswordHash())) {
            // Start a new session or reuse the existing one
            HttpSession session = request.getSession(true);

            // Store customer object in session for future reference
            session.setAttribute("loggedInCustomer", customer);

            // Store customerID in session for future use
            session.setAttribute("customerID", customer.getCustomerID());

            // Retrieve and store notifications in session
            List<String> notifications = BookingNotificationManager.getInstance().getNotificationsForCustomer(customer.getCustomerID());
            session.setAttribute("notifications", notifications);
            BookingNotificationManager.getInstance().clearNotificationsForCustomer(customer.getCustomerID());

            // Redirect to the customer dashboard

            response.sendRedirect("customer/customer-dashboard.jsp");
        } else {
            // If login fails, set the error message and forward to login page
            request.setAttribute("loginError", "Invalid username or password.");
            request.getRequestDispatcher("customer/customer-login.jsp").forward(request, response);
        }
    }

    private void registerCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Customer customer = new Customer();

        // Handle file upload for image
        Part imagePart = request.getPart("imageFile");
        String imageData;
        if (imagePart != null && imagePart.getSize() > 0) {
            try (InputStream is = imagePart.getInputStream()) {
                imageData = ImageUtil.convertToBase64(is);
            }
        } else {
            imageData = ImageUtil.getDefaultImage();
        }
        customer.setImageData(imageData);

        customer.setName(request.getParameter("name"));
        customer.setUsername(request.getParameter("username"));
        customer.setEmail(request.getParameter("email"));
        customer.setNic(request.getParameter("nic"));
        customer.setPhoneNumber(request.getParameter("phoneNumber"));
        customer.setAddress(request.getParameter("address"));

        // Hash the password using BCrypt if provided
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            customer.setPasswordHash(BcryptUtil.hashPassword(password));
        }

        customerService.addCustomer(customer);
        response.sendRedirect("customer/customer-login.jsp");
    }


}
