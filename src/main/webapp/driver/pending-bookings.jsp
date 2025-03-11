<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.model.Booking" %>
<%@ page import="org.megacitycab.service.driver.DriverService" %>
<%@ page import="org.megacitycab.model.Driver" %>
<%
    // Retrieve the logged-in driver from session
    Driver driver = (Driver) session.getAttribute("loggedInDriver");
    if (driver == null) {
        response.sendRedirect("driver-login.jsp");
        return;
    }

    List<Booking> pendingBookings = (List<Booking>) request.getAttribute("pendingBookings");

    // If pendingBookings are not set, load them now
    if (pendingBookings == null) {
        DriverService driverService = new DriverService();
        Integer driverID = (Integer) session.getAttribute("driverID"); // Use implicit session object

        if (driverID != null) {
            pendingBookings = driverService.getPendingBookingsForDriver(driverID);
            request.setAttribute("pendingBookings", pendingBookings);
        }
    }
%>


<!DOCTYPE html>
<html>
<head>
    <title>Pending Bookings</title>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
            background-attachment: fixed;
        }

        .container {
            margin-top: 50px;
            max-width: 900px;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 20px;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
        }

        h2 {
            text-align: center;
            margin-bottom: 20px;
            font-weight: bold;
        }

        table {
            margin-top: 20px;
        }

        .table th {
            background-color: #134378;
            color: white;
            text-align: center;
            font-weight: 600;
        }
        .btn-accept {
            background-color: #28a745;
            color: white;
            border: none;
            padding: 5px 15px;
            border-radius: 8px;
            transition: 0.3s;
        }

        .btn-accept:hover {
            background-color: #218838;
        }

        .alert-danger {
            margin-top: 10px;
        }
    </style>

</head>
<body>
<%@include file="driver-header.jsp"%>
<div class="container">
<h2>Pending Bookings</h2>

<c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("message"); %>
</c:if>

<!-- Display error message if present -->
<c:if test="${not empty param.error}">
    <div class="alert alert-danger" role="alert">
        Unable to accept booking. It may conflict with one of your existing accepted bookings.
    </div>
</c:if>

<table class="table table-striped table-bordered table-hover">
    <thead class="table-dark">
    <tr>
        <th>Booking ID</th>
        <th>Pickup Address</th>
        <th>Dropoff Address</th>
        <th>Booking Date</th>
        <th>Booking EndTime</th>
        <th>Distance (km)</th>
        <th>Action</th>
    </tr>
    </thead>
    <tbody>
    <% if (pendingBookings != null && !pendingBookings.isEmpty()) { %>
    <% for (Booking booking : pendingBookings) { %>
    <tr>
        <td><%= booking.getBookingID() %></td>
        <td><%= booking.getPickupAddress() %></td>
        <td><%= booking.getDropoffAddress() %></td>
        <td><%= booking.getBookingDate() %></td>
        <td><%= booking.getBookingEndTime() %></td>
        <td><%= booking.getDistanceKm() %></td>
        <td>
            <form action="${pageContext.request.contextPath}/pending-bookings" method="post">
                <input type="hidden" name="bookingID" value="<%= booking.getBookingID() %>">
                <button type="submit"class="btn-accept">Accept</button>
            </form>
        </td>
    </tr>
    <% } %>
    <% } else { %>
    <tr><td colspan="6">No pending bookings available.</td></tr>
    <% } %>
    </tbody>
</table>
</div>
<%@include file="driver-footer.jsp"%>
</body>
</html>
