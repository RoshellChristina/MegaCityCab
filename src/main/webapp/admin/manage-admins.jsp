<%@ page import="org.megacitycab.model.Admin" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="org.megacitycab.util.ImageUtil" %>
<%@ page import="org.megacitycab.service.admin.AdminServiceImpl" %>
<%@ page import="org.megacitycab.service.admin.AdminService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    // If customers are not set (for example, after a redirect), load them now.
    List<Admin> admin = (List<Admin>) request.getAttribute("adminList");
    if (admin == null) {
        AdminService adminService = new AdminServiceImpl();
        admin = adminService.getAllAdmins();
        request.setAttribute("adminList", admin);
    }
%>


<html>
<head>
    <title>Manage Admins</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmDelete(adminID) {
            if (confirm("Are you sure you want to delete this admin?")) {
                document.getElementById('deleteAdminID').value = adminID;
                document.getElementById('deleteForm').submit();
            }
        }

    </script>

    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
        }
    </style>

    <!-- Hidden form for deletion -->
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" id="deleteAdminID">
    </form>
</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container mt-4">
    <h2>Manage Admins</h2>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>
    <!-- Add Admin Button -->
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addAdminModal">➕ Add New Admin</button>

    <!-- Admin Table -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>AdminID</th>
            <th>Username</th>
            <th>Name</th>
            <th>Email</th>
            <th>Date Registered</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="admin" items="${adminList}">
            <tr>
                <td>${admin.adminId}</td>
                <td>${admin.username}</td>
                <td>${admin.name}</td>
                <td>${admin.email}</td>
                <td>${admin.dateRegistered}</td>
                <td>

                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${admin.adminId})">🗑️</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Add Admin Modal -->
<div class="modal fade" id="addAdminModal" tabindex="-1" aria-labelledby="addAdminModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title" id="addAdminModalLabel">Add Admin</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="insert">

                    <div class="mb-3">
                        <label for="username" class="form-label">Username:</label>
                        <input type="text" id="username" name="username" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="name" class="form-label">Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email:</label>
                        <input type="email" id="email" name="email" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password:</label>
                        <input type="password" id="password" name="password" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Add Admin</button>
                </div>
            </form>
        </div>
    </div>
</div>



<%@ include file="admin-footer.jsp" %>
</body>
</html>
