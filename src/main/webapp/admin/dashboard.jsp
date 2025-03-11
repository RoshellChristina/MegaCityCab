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
                            Total Revenue
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

