package org.megacitycab.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.megacitycab.service.admin.VehicleCategoryService;

import java.io.IOException;
import java.io.InputStream;

@WebServlet("/VehicleCategoryServlet")
@MultipartConfig(maxFileSize = 1024 * 1024 * 5) // Limit to 5MB
public class VehicleCategoryServlet extends HttpServlet {
    private final VehicleCategoryService categoryService = new VehicleCategoryService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                String name = request.getParameter("categoryName");
                double price = Double.parseDouble(request.getParameter("price"));
                Part filePart = request.getPart("image"); // Get the image file (can be null)
                byte[] imageData = null;

                // If the file part is not null, process the file
                if (filePart != null && filePart.getSize() > 0) {
                    InputStream fileContent = filePart.getInputStream();
                    imageData = fileContent.readAllBytes(); // Convert to byte array
                }

                request.getSession().setAttribute("message", "Category added successfully!");
                categoryService.addCategory(name, imageData, price);

            } else if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("categoryID"));
                String name = request.getParameter("categoryName");
                double price = Double.parseDouble(request.getParameter("price"));
                Part filePart = request.getPart("image");
                byte[] imageData = null;

                // If the file part is not null, process the file
                if (filePart != null && filePart.getSize() > 0) {
                    InputStream fileContent = filePart.getInputStream();
                    imageData = fileContent.readAllBytes();
                }

                request.getSession().setAttribute("message", "Category updated successfully!");
                categoryService.updateCategory(id, name, imageData, price);

            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("categoryID"));
                categoryService.deleteCategory(id);
            }

            // Redirect to the list of categories after any action
            response.sendRedirect("admin/manage-vehicle-categories.jsp");

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid number format! Please enter a valid price.");
            response.sendRedirect("admin/manage-vehicle-categories.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("message", "An unexpected error occurred. Please try again.");
            response.sendRedirect("admin/manage-vehicle-categories.jsp");
        }
    }


    // ✅ Handle GET request for deletion
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("categoryID"));
                categoryService.deleteCategory(id);
                request.getSession().setAttribute("message", "Category deleted successfully!");
                response.sendRedirect(request.getContextPath() + "/admin/manage-vehicle-categories.jsp");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.jsp"); // Redirect to an error page if needed
            }
        }
    }
}
