package org.megacitycab.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.megacitycab.model.Admin;
import org.megacitycab.service.admin.AdminService;
import org.megacitycab.service.admin.AdminServiceImpl;
import org.megacitycab.util.BcryptUtil;
import org.megacitycab.util.ImageUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@WebServlet("/admin")
@MultipartConfig(maxFileSize = 16177215) // about 16MB
public class AdminController extends HttpServlet {
    private AdminService adminService = new AdminServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            listAdmins(request, response);
        } else {
            switch (action) {
                case "new":
                    showNewForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteAdmin(request, response);
                    break;
                default:
                    listAdmins(request, response);
                    break;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle delete via POST as well.
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }
        switch (action) {
            case "insert":
                insertAdmin(request, response);
                break;
            case "update":
                updateAdmin(request, response);
                break;
            case "delete":
                deleteAdmin(request, response);
                break;
            default:
                listAdmins(request, response);
                break;
        }
    }

    private void listAdmins(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Admin> adminList = adminService.getAllAdmins();

        for (Admin admin : adminList) {
            if (admin.getImageData() == null || admin.getImageData().length == 0) {
                admin.setImageData(ImageUtil.getDefaultImageBytes()); // Assign default image
            }
        }

        request.setAttribute("adminList", adminList);
        request.getRequestDispatcher("admin/manage-admins.jsp").forward(request, response);
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("admin/admin-form.jsp").forward(request, response);
    }

    private void insertAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Admin admin = new Admin();

        // Handle file upload for image
        admin.setImageData(ImageUtil.getDefaultImageBytes());


        admin.setUsername(request.getParameter("username"));
        admin.setName(request.getParameter("name"));
        admin.setEmail(request.getParameter("email"));

        // Hash the password if provided
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            admin.setPasswordHash(BcryptUtil.hashPassword(password));
        }
        adminService.addAdmin(admin);
        response.sendRedirect("admin");
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Admin admin = adminService.getAdminById(id);
        request.setAttribute("admin", admin);
        request.getRequestDispatcher("admin/admin-form.jsp").forward(request, response);
    }

    private void updateAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Admin admin = new Admin();
        admin.setAdminId(id);

        // Handle image update if new image is uploaded

        admin.setUsername(request.getParameter("username"));
        admin.setName(request.getParameter("name"));
        admin.setEmail(request.getParameter("email"));

        // Update password if provided; otherwise, keep the current hash.
        String password = request.getParameter("password");
        if (password != null && !password.trim().isEmpty()) {
            admin.setPasswordHash(BcryptUtil.hashPassword(password));
        } else {
            Admin existing = adminService.getAdminById(id);
            admin.setPasswordHash(existing.getPasswordHash());
        }
        adminService.updateAdmin(admin);
        response.sendRedirect("admin");
    }

    private void deleteAdmin(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        adminService.deleteAdmin(id);
        response.sendRedirect("admin");
    }
}

