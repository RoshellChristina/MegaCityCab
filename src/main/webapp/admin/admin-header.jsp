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
            <li><a href="${pageContext.request.contextPath}/dashboard">Dashboard</a></li>

            <li><a href="<c:url value='/admin/manage-vehicle-categories.jsp' />">Vehicle Categories</a></li>
            <li><a href="<c:url value='/admin/manage-vehicles.jsp' />">Vehicles</a></li>
            <li><a href="${pageContext.request.contextPath}/DriverServlet?action=list">Drivers</a></li>
            <li><a href="<c:url value='/admin/manage-admins.jsp' />">Staff</a></li>
            <li><a href="<c:url value='/admin/manage-customers.jsp' />">Customers</a></li>
            <li><a href="<c:url value='/admin/manage-reviews.jsp' />">Reviews</a></li>
            <li><a href="<c:url value='/admin/manage-profile.jsp' />">Profile</a></li>
            <li><a href="<c:url value='/admin/logout.jsp' />">Logout</a></li>
        </ul>
    </nav>
</header>

