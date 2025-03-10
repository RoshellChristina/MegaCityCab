<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Admin Login</title>
</head>
<body>
<h2>Admin Login</h2>
<% if (request.getParameter("error") != null) { %>
<p style="color:red;">Invalid username or password!</p>
<% } %>
<form action="${pageContext.request.contextPath}/AdminLoginServlet" method="post">
  <label>Username:</label>
  <input type="text" name="username" required><br>
  <label>Password:</label>
  <input type="password" name="password" required><br>
  <button type="submit">Login</button>
</form>
</body>
</html>
