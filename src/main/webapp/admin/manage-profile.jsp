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
</head>
<body>
<%@ include file="admin-header.jsp" %>

<h2>Manage Profile</h2>

<form action="<%= request.getContextPath() %>/AdminProfileServlet" method="POST">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="<%= admin.getUsername() %>" required /><br><br>

    <label for="email">Email:</label>
    <input type="email" id="email" name="email" value="<%= admin.getEmail() %>" required /><br><br>

    <label for="password">New Password:</label>
    <input type="password" id="password" name="password" /><br><br>

    <input type="submit" value="Update Profile" />
</form>

<a href="dashboard.jsp">Back to Dashboard</a>

<%@ include file="admin-footer.jsp" %>
</body>
</html>


