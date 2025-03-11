package org.megacitycab.controller.customer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.megacitycab.model.Customer;
import org.megacitycab.service.customer.CustomerService;
import org.megacitycab.service.customer.CustomerServiceImpl;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;

import java.io.IOException;
import java.io.InputStream;
@WebServlet("/customerProfile")
@MultipartConfig
public class CustomerProfileServlet extends HttpServlet {

    private CustomerService customerService = new CustomerServiceImpl();

    // Handle GET request (load customer profile page)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the customer ID from the session
        Integer id = (Integer) request.getSession().getAttribute("customerID");
        if (id == null) {
            response.sendRedirect("customer/customer-login.jsp"); // Redirect to login if no ID is found
            return;
        }

        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            request.setAttribute("customer", customer);
            request.getRequestDispatcher("/customer/manage-profile.jsp").forward(request, response);
        } else {
            response.sendRedirect("customer/customer-login.jsp"); // Redirect to login if customer is not found
        }
    }

    // Handle POST request (update profile or delete account)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get the customer ID from the session
        Integer id = (Integer) request.getSession().getAttribute("customerID");
        if (id == null) {
            response.sendRedirect("customer/customer-login.jsp"); // Redirect to login if no ID is found
            return;
        }

        String action = request.getParameter("action"); // Determine if it's an update or delete
        if ("delete".equals(action)) {
            // Handle delete account
            customerService.deleteCustomer(id);
            request.getSession().invalidate(); // Invalidate session after account deletion
            response.sendRedirect("customer/customer-login.jsp");
            return;
        }

        // If not delete, proceed with profile update
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

        request.getSession().setAttribute("loggedInCustomer", customer);
        request.getSession().setAttribute("message", "Profile updated successfully!");
        response.sendRedirect(request.getContextPath()+"/customerProfile");  // Redirect to the same page to show up

    }
}
