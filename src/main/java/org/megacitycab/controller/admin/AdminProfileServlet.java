package org.megacitycab.controller.admin;

import org.megacitycab.model.Admin;
import org.megacitycab.service.admin.AdminService;
import org.megacitycab.service.admin.AdminServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.megacitycab.util.BcryptUtil;

import java.io.IOException;

@WebServlet("/AdminProfileServlet")
public class AdminProfileServlet extends HttpServlet {
    private final AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Admin admin = (Admin) session.getAttribute("adminUser");

        if (admin == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Retrieve updated info from the form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Update the admin's details
        admin.setUsername(username);
        admin.setEmail(email);

        if (password != null && !password.isEmpty()) {
            // Hash the new password (assuming you have a utility for hashing)
            String hashedPassword = BcryptUtil.hashPassword(password);
            admin.setPasswordHash(hashedPassword);
        }

        // Call the service to update the admin profile
        boolean isUpdated = adminService.updateAdminProfile(admin);

        if (isUpdated) {
            session.setAttribute("adminUser", admin);
            request.getSession().setAttribute("message", "Admin updated successfully!");// Update session with new info
            response.sendRedirect("admin/manage-profile.jsp?success=true");
        } else {
            request.getSession().setAttribute("message", "Failed to update admin!");
            response.sendRedirect("admin/manage-profile.jsp?error=true");
        }
    }
}

