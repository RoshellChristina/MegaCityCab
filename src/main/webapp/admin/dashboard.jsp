
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
    <link rel="stylesheet" href="<c:url value='/css/styles.css'/>">

    <!-- Include Chart.js CDN -->
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <!-- Include Bootstrap (Optional) for Layout -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>
<%@include file="admin-header.jsp"%>
<div class="container mt-5">
    <!-- Check if admin is logged in -->
    <c:if test="${not empty sessionScope.adminUser}">
        <h1>Admin Dashboard</h1>
        <div class="row">
            <!-- Total Revenue -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Revenue</h5>
                        <p class="card-text">$<c:out value="${totalRevenue}"/></p>
                    </div>
                </div>
            </div>

            <!-- Booking Status Counts -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Booking Status Counts</h5>
                        <ul class="list-group">
                            <li class="list-group-item">Pending: <c:out value="${pendingBookings}"/></li>
                            <li class="list-group-item">Accepted: <c:out value="${acceptedBookings}"/></li>
                            <li class="list-group-item">In Progress: <c:out value="${inProgressBookings}"/></li>
                            <li class="list-group-item">Completed: <c:out value="${completedBookings}"/></li>
                            <li class="list-group-item">Cancelled: <c:out value="${cancelledBookings}"/></li>
                        </ul>
                    </div>
                </div>
            </div>

            <!-- Total Drivers and Vehicles -->
            <div class="col-md-4">
                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title">Total Drivers and Vehicles</h5>
                        <ul class="list-group">
                            <li class="list-group-item">Total Drivers: <c:out value="${totalDrivers}"/></li>
                            <li class="list-group-item">Total Vehicles: <c:out value="${totalVehicles}"/></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="row mt-4">
            <!-- Monthly Revenue Graph -->
            <div class="col-md-6">
                <h4>Monthly Revenue</h4>
                <canvas id="monthlyRevenueChart"></canvas>
            </div>

            <!-- Most Demanded Vehicle Category -->
            <div class="col-md-6">
                <h4>Most Demanded Vehicle Category</h4>
                <p><c:out value="${mostDemandedCategory}"/></p>
            </div>
        </div>
    </c:if>

    <!-- If not logged in, show error -->
    <c:if test="${empty sessionScope.adminUser}">
        <h2>You must be logged in to view the dashboard.</h2>
    </c:if>
</div>

<!-- Script to render graphs -->
<script>
    // Monthly Revenue Chart
    var monthlyRevenueMonths = ['${monthlyRevenueMonths}'].split(',');
    var monthlyRevenue = ['${monthlyRevenue}'].split(',');

    var ctx = document.getElementById('monthlyRevenueChart').getContext('2d');
    var monthlyRevenueChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: monthlyRevenueMonths,
            datasets: [{
                label: 'Monthly Revenue',
                data: monthlyRevenue,
                borderColor: '#4e73df',
                backgroundColor: 'rgba(78, 115, 223, 0.2)',
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
</script>

<!-- Optional Bootstrap JS (for responsive layout, etc.) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<%@include file="admin-footer.jsp"%>
</body>

</html>



