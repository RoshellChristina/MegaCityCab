<%@ page import="org.megacitycab.model.Customer" %>
<%@ page import="org.megacitycab.service.customer.CustomerService" %>
<%@ page import="org.megacitycab.service.customer.CustomerServiceImpl" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // If customers are not set (for example, after a redirect), load them now.
    List<Customer> customers = (List<Customer>) request.getAttribute("customerList");
    if (customers == null) {
        CustomerService customerService = new CustomerServiceImpl();
        customers = customerService.getAllCustomers();
        request.setAttribute("customerList", customers);
    }
%>
<html>
<head>
    <title>Manage Customers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function confirmDelete(customerID) {
            if (confirm("Are you sure you want to delete this customer?")) {
                document.getElementById('deleteCustomerID').value = customerID;
                document.getElementById('deleteForm').submit();
            }
        }

        function openEditModal(customerID, name, username, email, nic, phone, address) {
            document.getElementById('editCustomerID').value = customerID;
            document.getElementById('editName').value = name;
            document.getElementById('editUsername').value = username;
            document.getElementById('editEmail').value = email;
            document.getElementById('editNIC').value = nic;
            document.getElementById('editPhone').value = phone;
            document.getElementById('editAddress').value = address;
            var editModal = new bootstrap.Modal(document.getElementById('editCustomerModal'));
            editModal.show();
        }
    </script>

    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
        }
    </style>

    <!-- Hidden form for deletion -->
    <form id="deleteForm" method="get" action="${pageContext.request.contextPath}/customer">

    <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" id="deleteCustomerID">
    </form>
</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container mt-4">
    <h2>Manage Customers</h2>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>
    <!-- Add Customer Button -->
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addCustomerModal">➕ Add New Customer</button>

    <!-- Customer Table -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>UserID</th>
            <th>Image</th>
            <th>Name</th>
            <th>Username</th>
            <th>Email</th>
            <th>NIC</th>
            <th>Phone</th>
            <th>Address</th>
            <th>Date Registered</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="customer" items="${customerList}">
            <tr>
                <td>${customer.userId}</td>
                <td>
                    <img src="data:image/png;base64,${customer.imageData}" alt="Customer Image" style="width:50px;height:50px;">
                </td>
                <td>${customer.name}</td>
                <td>${customer.username}</td>
                <td>${customer.email}</td>
                <td>${customer.nic}</td>
                <td>${customer.phoneNumber}</td>
                <td>${customer.address}</td>
                <td>${customer.dateRegistered}</td>
                <td>
                    <button class="btn btn-warning btn-sm"
                            onclick="openEditModal('${customer.userId}', '${customer.name}', '${customer.username}', '${customer.email}', '${customer.nic}', '${customer.phoneNumber}', '${customer.address}')">
                        📝 Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${customer.userId})">🗑️</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Add Customer Modal -->
<div class="modal fade" id="addCustomerModal" tabindex="-1" aria-labelledby="addCustomerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/customer" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title" id="addCustomerModalLabel">Add Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="insert">
                    <div class="mb-3">
                        <label for="imageFile" class="form-label">Image:</label>
                        <input type="file" id="imageFile" name="imageFile" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="name" class="form-label">Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="username" class="form-label">Username:</label>
                        <input type="text" id="username" name="username" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email:</label>
                        <input type="email" id="email" name="email" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="nic" class="form-label">NIC:</label>
                        <input type="text" id="nic" name="nic" class="form-control" maxlength="12" pattern="\d{12}" title="NIC must be 12 digits" required>
                    </div>
                    <div class="mb-3">
                        <label for="phoneNumber" class="form-label">Phone Number:</label>
                        <input type="text" id="phoneNumber" name="phoneNumber" class="form-control" maxlength="10" pattern="\d{10}" title="Phone number must be 10 digits" required>
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address:</label>
                        <input type="text" id="address" name="address" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">Password:</label>
                        <input type="password" id="password" name="password" class="form-control" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">Add Customer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Edit Customer Modal -->
<div class="modal fade" id="editCustomerModal" tabindex="-1" aria-labelledby="editCustomerModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/customer" method="post" enctype="multipart/form-data">
                <div class="modal-header">
                    <h5 class="modal-title" id="editCustomerModalLabel">Edit Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" id="editCustomerID" name="id">
                    <div class="mb-3">
                        <label for="imageFile" class="form-label">Change Image (optional):</label>
                        <input type="file" id="editImageFile" name="imageFile" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editName" class="form-label">Name:</label>
                        <input type="text" id="editName" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editUsername" class="form-label">Username:</label>
                        <input type="text" id="editUsername" name="username" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email:</label>
                        <input type="email" id="editEmail" name="email" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editNIC" class="form-label">NIC:</label>
                        <input type="text" id="editNIC" name="nic" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editPhone" class="form-label">Phone Number:</label>
                        <input type="text" id="editPhone" name="phoneNumber" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editAddress" class="form-label">Address:</label>
                        <input type="text" id="editAddress" name="address" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">New Password (leave blank to keep current):</label>
                        <input type="password" id="editPassword" name="password" class="form-control">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Update Customer</button>
                </div>
            </form>
        </div>
    </div>
</div>

<%@ include file="admin-footer.jsp" %>
</body>
</html>

