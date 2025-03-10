<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Customer Login</title>
  <link rel="stylesheet" href="css/style.css"> <!-- Add your CSS file here -->
</head>
<body>
<div class="container">
  <h2>Customer Login</h2>
  <form action="${pageContext.request.contextPath}/login" method="post">
    <input type="hidden" name="action" value="login">

    <label for="username">Username:</label>
    <input type="text" id="username" name="username" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>

    <button type="submit">Login</button>
  </form>

  <p>Don't have an account? <a href="customer-registration.jsp">Register here</a></p>

  <%-- Display error message if login fails --%>
  <% if (request.getAttribute("loginError") != null) { %>
  <p style="color: red;"><%= request.getAttribute("loginError") %></p>
  <% } %>
</div>
</body>
</html>

