package org.megacitycab.controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.megacitycab.model.Customer;
import org.megacitycab.observer.BookingNotificationManager;
import org.megacitycab.service.customer.CustomerService;
import org.megacitycab.service.customer.CustomerServiceImpl;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/customer")
@MultipartConfig(maxFileSize = 16177215) // about 16MB
public class CustomerController extends HttpServlet {
    private CustomerService customerService = new CustomerServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            listCustomers(request, response);
        } else {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCustomer(request, response);
                    break;
                default:
                    listCustomers(request, response);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Because of multipart form, parameters may come via parts.
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "insert":
                insertCustomer(request, response);
                break;
            case "register": // Customer-side registration
                registerCustomer(request, response);
                break;
            case "login": // Customer login
                loginCustomer(request, response);
                break;
            case "update":
                updateCustomer(request, response);
                break;
            case "delete":
                deleteCustomer(request, response);
                break;
            default:
                listCustomers(request, response);
                break;
        }
    }

    private void listCustomers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Customer> list = customerService.getAllCustomers();
        request.setAttribute("customerList", list);
        request.getRequestDispatcher("admin/manage-customers.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("admin/customer-form.jsp").forward(request, response);
    }

    private void insertCustomer(HttpServletRequest request, HttpServletResponse response)
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
        response.sendRedirect("customer");
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

    private void loginCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Fetch the customer from the database by username
        Customer customer = customerService.getCustomerByUsername(username);

        // Check if customer exists and if the password is correct
        if (customer != null && BcryptUtil.verifyPassword(password, customer.getPasswordHash())) {
            // Start a new session or reuse the existing one
            HttpSession session = request.getSession(true); // true to create a new session if none exists

            // Store customer object in session for future reference
            session.setAttribute("loggedInCustomer", customer);

            // Store customerID in session for future use
            session.setAttribute("customerID", customer.getCustomerID());  // Store customerID

            // Retrieve any stored notifications for this customer and save them in session.
            List<String> notifications = BookingNotificationManager.getInstance().getNotificationsForCustomer(customer.getCustomerID());
            session.setAttribute("notifications", notifications);
            // Clear the notifications after retrieving them.
            BookingNotificationManager.getInstance().clearNotificationsForCustomer(customer.getCustomerID());

            // Redirect to the customer dashboard
            response.sendRedirect("customer/customer-dashboard.jsp");
        } else {
            // If login fails, forward to the login page with an error message
            request.setAttribute("loginError", "Invalid username or password.");
            request.getRequestDispatcher("customer/customer-login.jsp").forward(request, response);
        }
    }




    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = customerService.getCustomerById(id);
        request.setAttribute("customer", customer);
        request.getRequestDispatcher("admin/customer-form.jsp").forward(request, response);
    }

    private void updateCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("id"));
        Customer customer = new Customer();
        customer.setUserId(id);

        // Handle image update if new image is uploaded
        Part imagePart = request.getPart("imageFile");
        String imageData;
        if (imagePart != null && imagePart.getSize() > 0) {
            try (InputStream is = imagePart.getInputStream()) {
                imageData = ImageUtil.convertToBase64(is);
            }
        } else {
            // Keep existing image if not updated
            Customer existing = customerService.getCustomerById(id);
            imageData = existing.getImageData();
        }
        customer.setImageData(imageData);

        customer.setName(request.getParameter("name"));
        customer.setUsername(request.getParameter("username"));
        customer.setEmail(request.getParameter("email"));
        customer.setNic(request.getParameter("nic"));
        customer.setPhoneNumber(request.getParameter("phoneNumber"));
        customer.setAddress(request.getParameter("address"));

        // Update password if provided; otherwise, keep existing hash.
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            customer.setPasswordHash(BcryptUtil.hashPassword(password));
        } else {
            Customer existing = customerService.getCustomerById(id);
            customer.setPasswordHash(existing.getPasswordHash());
        }
        customerService.updateCustomer(customer);
        response.sendRedirect("customer");
    }

    private void deleteCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        customerService.deleteCustomer(id);
        response.sendRedirect("customer");
    }
}
