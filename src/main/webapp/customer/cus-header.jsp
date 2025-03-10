<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/styles.css' />">
</head>
<body>
<header>
    <div class="logo">
        <h1>MegaCityCab Admin</h1>
    </div>
    <nav>
        <ul>
            <li><a href="<c:url value='/customer/customer-dashboard.jsp' />">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/customerbooking?action=view">My Bookings</a></li>
            <li><a href="<c:url value='/customer/booking-form.jsp' />">Add Bookings</a></li>
            <li><a href="<c:url value='/customer/manage-reviews.jsp' />">My Reviews</a></li>
            <li><a href="<c:url value='/customer/manage-profile.jsp' />">Profile</a></li>
            <li><a href="<c:url value='/customer/customer-logout.jsp' />">Logout</a></li>
        </ul>
    </nav>
</header>

