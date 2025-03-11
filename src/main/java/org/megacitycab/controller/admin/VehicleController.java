package org.megacitycab.controller.admin;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.megacitycab.model.Vehicle;
import org.megacitycab.model.VehicleCategory;
import org.megacitycab.service.admin.VehicleCategoryService;
import org.megacitycab.service.admin.VehicleService;

import java.io.IOException;
import java.util.List;

@WebServlet("/VehicleServlet")
@MultipartConfig
public class VehicleController extends HttpServlet {
    private VehicleService vehicleService;
    private VehicleCategoryService categoryService;

    @Override
    public void init() throws ServletException {
        vehicleService = new VehicleService();
        categoryService = new VehicleCategoryService();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            addVehicle(request, response);
        } else if ("edit".equals(action)) {
            updateVehicle(request, response);
        } else if ("delete".equals(action)) {
            deleteVehicle(request, response);
        }else if ("registerVehicle".equals(action)) {
            registerVehicle(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || "list".equals(action)) {
            listVehicles(request, response);
        } else if ("toggle".equals(action)) {
            toggleStatus(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    private void addVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String licensePlate = request.getParameter("licensePlate");
            String color = request.getParameter("color");

            Vehicle vehicle = new Vehicle();
            vehicle.setCategoryID(categoryID);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setColor(color);
            vehicle.setStatus("Inactive"); // Default status

            if (vehicleService.addVehicle(vehicle)) {
                request.getSession().setAttribute("message", "Vehicle added successfully!");
                response.sendRedirect("admin/manage-vehicles.jsp?success=Vehicle added");
            } else {
                request.getSession().setAttribute("message", "Failed to add vehicle! Please try again.");
                response.sendRedirect("admin/manage-vehicles.jsp?error=Failed to add vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/manage-vehicles.jsp?error=Invalid input");
        }
    }



    private void updateVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            String licensePlate = request.getParameter("licensePlate");
            String color = request.getParameter("color");
            String status = request.getParameter("status");

            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleID(vehicleID);
            vehicle.setCategoryID(categoryID);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setColor(color);
            vehicle.setStatus(status);

            if (vehicleService.updateVehicle(vehicle)) {
                request.getSession().setAttribute("message", "Vehicle updated successfully!");
                response.sendRedirect("admin/manage-vehicles.jsp?success=Vehicle updated");
            } else {
                request.getSession().setAttribute("message", "Failed to update vehicle! Please try again.!");
                response.sendRedirect("admin/manage-vehicles.jsp?error=Failed to update vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/manage-vehicles.jsp?error=Invalid input");
        }
    }

    private void deleteVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            if (vehicleService.deleteVehicle(vehicleID)) {
                request.getSession().setAttribute("message", "Vehicle deleted successfully!");
                response.sendRedirect("admin/manage-vehicles.jsp?success=Vehicle deleted");
            } else {
                request.getSession().setAttribute("message", "Failed to delete vehicle! Please try again.");
                response.sendRedirect("admin/manage-vehicles.jsp?error=Failed to delete vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin/manage-vehicles.jsp?error=Invalid request");
        }
    }

    private void listVehicles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check for a filter parameter
        String filterCategoryID = request.getParameter("filterCategoryID");
        List<Vehicle> vehicles;

        if (filterCategoryID != null && !filterCategoryID.isEmpty()) {
            int catID = Integer.parseInt(filterCategoryID);
            vehicles = vehicleService.getVehiclesByCategory(catID);
        } else {
            vehicles = vehicleService.getAllVehicles();
        }

        // Fetch all categories for the dropdown
        List<VehicleCategory> categories = VehicleCategoryService.getAllCategories();
        request.setAttribute("vehicles", vehicles);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategoryID", filterCategoryID); // For pre-selecting the filter

        request.getRequestDispatcher("/admin/manage-vehicles.jsp").forward(request, response);
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int vehicleID = Integer.parseInt(request.getParameter("vehicleID"));
            Vehicle vehicle = vehicleService.getVehicleById(vehicleID);
            if (vehicle != null) {
                String currentStatus = vehicle.getStatus();
                String newStatus;
                if ("Active".equalsIgnoreCase(currentStatus)) {
                    newStatus = "Inactive";
                } else if ("Inactive".equalsIgnoreCase(currentStatus)) {
                    newStatus = "Out-of-Order";
                } else if ("Out-of-Order".equalsIgnoreCase(currentStatus)) {
                    newStatus = "Active";
                } else {
                    // If the current status is unexpected, default to Active.
                    newStatus = "Active";
                }
                vehicle.setStatus(newStatus);
                if (vehicleService.updateVehicle(vehicle)) {
                    request.getSession().setAttribute("message", "Vehicle status updated successfully!");
                    response.sendRedirect(request.getContextPath() + "/admin/manage-vehicles.jsp?success=Status toggled");
                } else {
                    request.getSession().setAttribute("message", "Failed to update vehicle status! Please try again.");
                    response.sendRedirect(request.getContextPath() + "/admin/manage-vehicles.jsp?error=Failed to toggle status");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/admin/manage-vehicles.jsp?error=Vehicle not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/manage-vehicles.jsp?error=Invalid request");
        }
    }

    private void registerVehicle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int categoryID = Integer.parseInt(request.getParameter("categoryID"));
            int driverID = Integer.parseInt(request.getParameter("driverID"));
            String licensePlate = request.getParameter("licensePlate");
            String color = request.getParameter("color");

            Vehicle vehicle = new Vehicle();
            vehicle.setCategoryID(categoryID);
            vehicle.setDriverID(driverID);
            vehicle.setLicensePlate(licensePlate);
            vehicle.setColor(color);
            vehicle.setStatus("Inactive");

            if (vehicleService.addVehicle(vehicle)) {
                response.sendRedirect("driver/driver-login.jsp?success=Vehicle registered");
            } else {
                response.sendRedirect("driver/register-vehicle.jsp?error=Failed to register vehicle");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("driver/register-vehicle.jsp?error=Invalid input");
        }


}}
