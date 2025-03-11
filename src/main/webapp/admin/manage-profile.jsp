<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.megacitycab.model.Admin" %>
<%@ page session="true" %>

<%
    // Retrieve the admin object from the session
    HttpSession sessionobj = request.getSession(false); // Use false to avoid creating a new session
    Admin admin = null;

    if (sessionobj != null) {
        admin = (Admin) sessionobj.getAttribute("adminUser"); // Retrieve the admin object
    }

    // If no admin object is found in the session, redirect to the login page
    if (admin == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<html>
<head>
    <title>Manage Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css">
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size:cover;

        }

        .custom-alert-orange {
            background-color: #FFA500;  /* Orange color */
            color: white;               /* Text color */
            border: 1px solid #ff8c00;  /* Border color for a consistent look */
        }


        h2 {
            text-align: center;
            color: #333;
        }

        .form-container {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 30px;
            max-width: 500px;
            margin: 0 auto;
        }

        .form-container input[type="text"],
        .form-container input[type="email"],
        .form-container input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 14px;
        }

        .form-container input[type="submit"] {
            background-color: #007bff;
            color: white;
            padding: 12px 20px;
            font-size: 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        .form-container input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .alert {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<%@ include file="admin-header.jsp" %>

<h2>Manage Profile</h2>
<c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${sessionScope.message}
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("message"); %>
</c:if>
<div class="container">
<div class="form-container">
<form action="<%= request.getContextPath() %>/AdminProfileServlet" method="POST">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="<%= admin.getUsername() %>" required /><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="<%= admin.getEmail() %>" required /><br><br>

    <label for="password">New Password:</label>
    <input type="password" id="password" name="password" /><br><br>

    <input type="submit" value="Update Profile" />
</form>
</div>
</div>
<%@ include file="admin-footer.jsp" %>
</body>
</html>


