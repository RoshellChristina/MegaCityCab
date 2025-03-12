<%@ page import="org.megacitycab.model.Booking" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    // Check if admin session exists
    org.megacitycab.model.Admin adminUser = (org.megacitycab.model.Admin) session.getAttribute("adminUser");
    if(adminUser == null){
        response.sendRedirect(request.getContextPath() + "login.jsp?error=notloggedin");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <!-- External CSS -->
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;

        }

        .container {
            margin-top: 50px;
            padding-bottom: 100px;
        }
        h2, h4 {
            color: #333;
        }
        .card {
            border: none;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        .card-body {
            padding: 20px;
        }
        .card-title {
            font-size: 1.25rem;
            margin-bottom: 15px;
        }
        .list-group-item {
            border: none;
            padding: 10px 15px;
            font-size: 1rem;
            background-color: #f8f9fc;
        }
        .icon-placeholder {
            width: 20px;
            height: 20px;
            margin-right: 8px;
        }

        .booking-container {
            background: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-top: 30px;
            overflow-x: auto;
        }
        .booking-table {
            width: 100%;
            border-collapse: collapse;
        }
        .booking-table th, .booking-table td {
            padding: 12px 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        .booking-table th {
            background-color: rgba(65, 105, 149, 0.9);
            font-weight: bold;
            color: #ffffff;
        }
        .booking-table tr:hover {
            background-color: #f1f1f1;
        }
        .no-bookings {
            text-align: center;
            padding: 20px;
            font-size: 1.1rem;
            color: #555;
        }
    </style>
</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container">
    <c:if test="${not empty sessionScope.adminUser}">
        <h2 class="mb-4">
            <!-- Replace with your icon image if needed -->
            Admin Dashboard
        </h2>
        <div class="row">
            <!-- Left Column: Total Revenue & Total Drivers/Vehicles -->
            <div class="col-md-4">
                <!-- Total Revenue -->
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">
                            <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/revenue.png" alt="Revenue Icon">
                            Total/Monthly Revenue
                        </h5>
                        <p class="card-text fs-4">$<c:out value="${totalRevenue}"/></p>
                    </div>
                </div>
                <!-- Total Drivers and Vehicles -->
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">
                            Total Drivers & Vehicles
                        </h5>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/driver.png" alt="Driver Icon">
                                Total Drivers: <c:out value="${totalDrivers}"/>
                            </li>
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/taxi.png" alt="Vehicle Icon">
                                Total Vehicles: <c:out value="${totalVehicles}"/>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Middle Column: Booking Status Counts -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">
                            <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/booking.png" alt="Booking Icon">
                            Booking Status Counts
                        </h5>
                        <ul class="list-group">
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/expired.png" alt="Pending Icon">
                                Pending: <c:out value="${pendingBookings}"/>
                            </li>
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/checked.png" alt="Accepted Icon">
                                Accepted: <c:out value="${acceptedBookings}"/>
                            </li>
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/check.png" alt="In Progress Icon">
                                In Progress: <c:out value="${inProgressBookings}"/>
                            </li>
                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/completed.png" alt="Completed Icon">
                                Completed: <c:out value="${completedBookings}"/>
                            </li>

                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/paid.png" alt="Paid Icon">
                                Paid: <c:out value="${paidBookings}"/>
                            </li>

                            <li class="list-group-item">
                                <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/cross.png" alt="Cancelled Icon">
                                Cancelled: <c:out value="${cancelledBookings}"/>
                            </li>

                        </ul>
                    </div>
                </div>
            </div>

            <!-- Right Column: Most Demanded Vehicle Category -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h4>
                            <img class="icon-placeholder" src="${pageContext.request.contextPath}/images/category.png" alt="Category Icon">
                            Most Demanded Vehicle Category
                        </h4>
                        <p class="fs-5"><c:out value="${mostDemandedCategory}"/></p>
                    </div>
                </div>
            </div>
        </div>


        <!-- Monthly Revenue Filter Form -->
        <div class="row mt-4">
            <div class="col-md-12">
                <form method="get" action="${pageContext.request.contextPath}/dashboard" class="row g-3 align-items-center">
                    <div class="col-auto">
                        <label for="month" class="col-form-label">Select Month:</label>
                    </div>
                    <div class="col-auto">
                        <select name="month" id="month" class="form-select">
                            <option value="">--All--</option>
                            <option value="January" ${selectedMonth == 'January' ? 'selected' : ''}>January</option>
                            <option value="February" ${selectedMonth == 'February' ? 'selected' : ''}>February</option>
                            <option value="March" ${selectedMonth == 'March' ? 'selected' : ''}>March</option>
                            <option value="April" ${selectedMonth == 'April' ? 'selected' : ''}>April</option>
                            <option value="May" ${selectedMonth == 'May' ? 'selected' : ''}>May</option>
                            <option value="June" ${selectedMonth == 'June' ? 'selected' : ''}>June</option>
                            <option value="July" ${selectedMonth == 'July' ? 'selected' : ''}>July</option>
                            <option value="August" ${selectedMonth == 'August' ? 'selected' : ''}>August</option>
                            <option value="September" ${selectedMonth == 'September' ? 'selected' : ''}>September</option>
                            <option value="October" ${selectedMonth == 'October' ? 'selected' : ''}>October</option>
                            <option value="November" ${selectedMonth == 'November' ? 'selected' : ''}>November</option>
                            <option value="December" ${selectedMonth == 'December' ? 'selected' : ''}>December</option>
                        </select>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">Filter</button>
                    </div>
                </form>
                <div class="container">
                    <div class="booking-container">
                <!-- Bookings Table -->
                        <h3>Bookings List</h3>
                        <table class="booking-table">
                            <thead>

                    <tr>
                        <th>Booking ID</th>
                        <th>User ID</th>
                        <th>Vehicle Category</th>
                        <th>Pickup Address</th>
                        <th>Dropoff Address</th>
                        <th>Booking Date</th>
                        <th>Distance (km)</th>
                        <th>Status</th>
                    </tr>
                            </thead>
                            <tbody>
                    <%
                        List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
                        if (bookings != null && !bookings.isEmpty()) {
                            for (Booking booking : bookings) {
                    %>
                    <tr>
                        <td><%= booking.getBookingID() %></td>
                        <td><%= booking.getUserID() %></td>
                        <td><%= booking.getVehicleCategoryID() %></td>
                        <td><%= booking.getPickupAddress() %></td>
                        <td><%= booking.getDropoffAddress() %></td>
                        <td><%= booking.getBookingDate() %></td>
                        <td><%= booking.getDistanceKm() %></td>
                        <td><%= booking.getStatus() %></td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="8" class="no-bookings">No bookings found for this month.</td>
                    </tr>
                    <%
                        }
                    %>
                    </tbody>
                </table>
            </div>
        </div>
            </div>
        </div>
    </c:if>
    <c:if test="${empty sessionScope.adminUser}">
        <div class="alert alert-danger mt-4" role="alert">
            You must be logged in to view the dashboard.
        </div>
    </c:if>
</div>

<!-- Optional Bootstrap JS (for responsive layout, etc.) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<%@ include file="admin-footer.jsp" %>
</body>
</html>

