<%@ page import="org.megacitycab.model.Driver" %>
<%@ page import="org.megacitycab.model.Vehicle" %>
<%@ page import="org.megacitycab.model.VehicleCategory" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.service.admin.VehicleCategoryService" %>
<%
    // Retrieve the logged-in driver from session
    Driver driver = (Driver) session.getAttribute("loggedInDriver");
    if (driver == null) {
        response.sendRedirect("driver-login.jsp");
        return;
    }

    List<VehicleCategory> categories = (List<VehicleCategory>) request.getAttribute("categories");
    if (categories == null) {
        VehicleCategoryService categoryService = new VehicleCategoryService();
        categories = categoryService.getAllCategories();
        request.setAttribute("categories", categories);
    }
    // For freelance drivers, retrieve any vehicle info loaded by the servlet
    Vehicle driverVehicle = (Vehicle) request.getAttribute("driverVehicle");

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Profile</title>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
            background-attachment: fixed;
        }

        h2 {
            text-align: center;
            color: #333;
            font-size: 2rem;
            margin-top: 20px;
        }

        form {
            background: #fff;
            max-width: 600px;
            margin: 30px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        label {
            font-size: 1rem;
            color: #555;
            margin: 10px 0 5px;
            display: block;
        }

        input[type="text"], input[type="email"], input[type="password"], select {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
            background-color: #f9f9f9;
        }

        button {
            width: 100%;
            padding: 12px;
            background-color: #134378;
            color: white;
            border: none;
            border-radius: 4px;
            font-size: 1.1rem;
            cursor: pointer;
            margin-top: 20px;
            transition: background-color 0.3s ease;
        }

        .container {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
            gap: 20px; /* Adjust the gap between the containers */
        }

        /* Profile form container */
        .profile-container {
            background: #fff;
            max-width: 60%;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* Vehicle form container */
        .vehicle-container {
            background: #fff;
            max-width: 60%;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }


        @media screen and (max-width: 768px) {
            .container {
                flex-direction: column;
                align-items: center;
            }

            .profile-container, .vehicle-container {
                max-width: 100%;
                margin-bottom: 20px;
            }
        }

        button:hover {
            background-color: #0056b3;
        }

        .divider {
            margin: 40px 0;
            border-top: 1px solid #ddd;
        }

        .form-group {
            margin-bottom: 20px;
        }

        .form-group select {
            width: calc(100% - 22px); /* To account for padding */
        }

        .section-header {
            font-size: 1.5rem;
            color: #333;
            margin-bottom: 15px;
        }

        .section-content {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
        }

        .section-content input[type="text"] {
            margin-bottom: 10px;
        }

    </style>
</head>
<body>
<%@include file="driver-header.jsp"%>
<div class="container">
    <div class="profile-container">
<h2>Manage Profile</h2>
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("message"); %>
        </c:if>
<!-- Profile Update Form -->
<form action="${pageContext.request.contextPath}/DriverProfileServlet" method="post">
    <input type="hidden" name="action" value="updateProfile">
        <div class="form-group">
            <label>Name:</label>
            <input type="text" name="name" value="<%= driver.getName() %>" required><br>
        </div>

        <div class="form-group">
            <label>Email:</label>
            <input type="email" name="email" value="<%= driver.getEmail() %>" required><br>
        </div>

        <div class="form-group">
            <label> Phone Number:</label>
            <input type="text" name="phoneNumber" value="<%= driver.getPhoneNumber() %>" required><br>
        </div>

        <div class="form-group">
            <label> Password (leave blank to keep current):</label>
            <input type="password" name="password"><br>
        </div>
    <button type="submit">Update Profile</button>
</form>
    </div>

<% if ("freelance".equalsIgnoreCase(driver.getEmpType())) { %>
    <div class="vehicle-container">
<hr class="divider">
    <div class="section-header">Manage Vehicle</div>
    <div class="section-content">
        <c:if test="${not empty sessionScope.message}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("message"); %>
        </c:if>
<form action="${pageContext.request.contextPath}/DriverProfileServlet" method="post">
    <% if (driverVehicle != null) { %>
    <!-- When updating existing vehicle info -->
    <input type="hidden" name="action" value="updateVehicle">
    <input type="hidden" name="vehicleID" value="<%= driverVehicle.getVehicleID() %>">
    <% } else { %>
    <!-- When adding vehicle info for the first time -->
    <input type="hidden" name="action" value="registerVehicle">
    <% } %>

    <div class="form-group">
    <label>Vehicle Category:</label>
    <select name="categoryID" required>
        <option value="">Select Category</option>
        <% if (categories != null) {
            for (VehicleCategory cat : categories) {
                String selected = (driverVehicle != null && cat.getCategoryID() == driverVehicle.getCategoryID()) ? "selected" : "";
        %>
        <option value="<%= cat.getCategoryID() %>" <%= selected %>><%= cat.getCategoryName() %></option>
        <%   }
        } %>
    </select><br>
    </div>
    <div class="form-group">
        <label> License Plate:</label>
        <input type="text" name="licensePlate" value="<%= driverVehicle != null ? driverVehicle.getLicensePlate() : "" %>" required><br>
    </div>

    <div class="form-group">
        <label> Color:</label>
        <input type="text" name="color" value="<%= driverVehicle != null ? driverVehicle.getColor() : "" %>" required><br>
    </div>
    <button type="submit"><%= (driverVehicle != null ? "Update Vehicle" : "Add Vehicle") %></button>
    </div>
    </div>
    <% } %>
</div>
<%@ include file="driver-footer.jsp"%>
</body>
</html>
