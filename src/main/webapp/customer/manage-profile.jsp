<%@ page import="org.megacitycab.model.Customer" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    // Retrieve the logged-in customer from the session
    Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
    if (loggedInCustomer == null) {
        response.sendRedirect("customer-login.jsp");
        return;
    }
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

        /* Profile form container */
        .profile-container {
            background: #fff;
            max-width: 600px;
            margin: 30px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }

        /* General form styling */
        .profile-container form {
            display: block;
        }

        label {
            font-size: 1rem;
            color: #555;
            margin: 10px 0 5px;
            display: block;
        }

        input[type="text"],
        input[type="email"],
        input[type="password"],
        textarea,
        input[type="file"] {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1rem;
            background-color: #f9f9f9;
        }

        /* Button styling */
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

        button:hover {
            background-color: #0056b3;
        }

        /* Profile image preview styling */
        .profile-container img {
            display: block;
            margin: 10px auto;
            border-radius: 50%;
            width: 150px;
            height: 150px;
        }

        /* Message styles */
        .profile-container .message {
            text-align: center;
            font-size: 1rem;
            margin: 10px 0;
        }

        .profile-container .message.success {
            color: green;
        }

        .profile-container .message.error {
            color: red;
        }

        /* Delete account button override */
        .profile-container .delete-button {
            background-color: red;
        }

        .profile-container .delete-button:hover {
            background-color: darkred;
        }

    </style>
</head>
<body>

<%@include file="cus-header.jsp"%>
<div class="profile-container">
<h2>Manage Your Profile</h2>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>

<!-- Profile Update Form -->
<form action="${pageContext.request.contextPath}/customerProfile" method="post" enctype="multipart/form-data">
    <input type="hidden" name="id" value="<%= loggedInCustomer.getCustomerID() %>">
    <input type="hidden" name="action" value="update"> <!-- Hidden field to indicate action -->

    <label for="name">Full Name:</label>
    <input type="text" id="name" name="name" value="<%= loggedInCustomer.getName() %>" required><br>

    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="<%= loggedInCustomer.getUsername() %>" required><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="<%= loggedInCustomer.getEmail() %>" required><br>

    <label for="nic">NIC:</label>
    <input type="text" id="nic" name="nic" value="<%= loggedInCustomer.getNic() %>" required><br>

    <label for="phoneNumber">Phone Number:</label>
    <input type="text" id="phoneNumber" name="phoneNumber" value="<%= loggedInCustomer.getPhoneNumber() %>" required><br>

    <label for="address">Address:</label>
    <textarea id="address" name="address" required><%= loggedInCustomer.getAddress() %></textarea><br>

    <label for="password">New Password (leave blank to keep current):</label>
    <input type="password" id="password" name="password"><br>

    <label for="imageFile">Profile Picture:</label>
    <input type="file" id="imageFile" name="imageFile"><br>
    <%-- Show current profile image if available --%>
    <img src="data:image/png;base64,<%= loggedInCustomer.getImageData() %>" alt="Profile Picture" style="width: 150px; height: 150px; border-radius: 50%;"><br>

    <button type="submit">Update Profile</button>
</form>

    <!-- Account Deletion Form -->
    <form id="deleteForm" action="${pageContext.request.contextPath}/customerProfile" method="post">
        <input type="hidden" name="id" value="<%= loggedInCustomer.getCustomerID() %>">
        <input type="hidden" name="action" value="delete"> <!-- Hidden field to indicate action -->
        <!-- The button now triggers the modal instead of submitting directly -->
        <button class="delete-button" type="button" id="deleteButton" style="background-color: red; color: white;">Delete Account</button>
    </form>

    <!-- Confirmation Modal -->
    <div id="confirmModal" class="modal-overlay" style="display:none; position: fixed; top:0; left:0; width:100%; height:100%; background-color: rgba(0,0,0,0.4);">
        <div class="modal-content" style="background: #fff; padding: 20px; border-radius: 8px; text-align: center; max-width: 400px; margin: 100px auto;">
            <h3>Confirm Account Deletion</h3>
            <p>Are you sure you want to delete your profile? This action cannot be undone.</p>
            <button id="confirmDelete" style="background-color: red; color: white; padding: 8px 16px; border: none; border-radius: 6px;">Yes, Delete</button>
            <button id="cancelDelete" style="background-color: #32649a; color: white; padding: 8px 16px; border: none; border-radius: 6px; margin-left: 10px;">Cancel</button>
        </div>
    </div>

    <script>
        // Show the confirmation modal when delete button is clicked
        document.getElementById('deleteButton').addEventListener('click', function() {
            document.getElementById('confirmModal').style.display = 'block';
        });

        // Hide the modal if cancel is clicked
        document.getElementById('cancelDelete').addEventListener('click', function() {
            document.getElementById('confirmModal').style.display = 'none';
        });

        // Submit the form if the user confirms deletion
        document.getElementById('confirmDelete').addEventListener('click', function() {
            document.getElementById('deleteForm').submit();
        });
    </script>


</div>
<%@include file="cus-footer.jsp"%>
</body>
</html>
