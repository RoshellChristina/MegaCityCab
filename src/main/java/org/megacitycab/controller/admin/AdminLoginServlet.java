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
import java.io.IOException;

@WebServlet("/AdminLoginServlet")
public class AdminLoginServlet extends HttpServlet {
    private final AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Admin admin = adminService.authenticateAdmin(username, password);

        if (admin != null) {
            HttpSession session = request.getSession();
            session.setAttribute("adminUser", admin);
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } else {
            response.sendRedirect("admin/login.jsp?error=invalid");
        }
    }
}



