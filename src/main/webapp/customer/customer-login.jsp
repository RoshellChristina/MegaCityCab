<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Customer Login</title>

  <style>
    body {
      font-family: Arial, sans-serif;
      background-image: url("<%= request.getContextPath() %>/images/img_2.png");
      background-size: cover;
      background-attachment: fixed;
      margin: 0;
      padding: 0;
    }

    .container {
      max-width: 400px;
      margin: 50px auto;
      background-color: rgba(255, 255, 255, 0.9);
      padding: 30px 20px;
      border-radius: 12px;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
      text-align: center;
    }

    h2 {
      margin-bottom: 20px;
      color: #333;
    }

    label {
      display: block;
      margin: 8px 0 4px;
      font-weight: bold;
      color: #555;
    }

    input[type="text"],
    input[type="email"],
    input[type="password"],
    input[type="file"] {
      width: 100%;
      padding: 10px;
      margin: 5px 0 15px;
      border: 1px solid #ddd;
      border-radius: 8px;
      box-sizing: border-box;
      font-size: 16px;
      transition: border-color 0.3s;
    }

    input[type="text"]:focus,
    input[type="email"]:focus,
    input[type="password"]:focus,
    input[type="file"]:focus {
      border-color: #134378;
      outline: none;
    }

    button {
      width: 100%;
      padding: 12px;
      background-color: #134378;
      color: white;
      border: none;
      border-radius: 8px;
      font-size: 18px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    button:hover {
      background-color: #32649a;
    }

    p {
      margin-top: 15px;
      color: #555;
    }

    a {
      color: #134378;
      text-decoration: none;
    }

    a:hover {
      text-decoration: underline;
    }

    .error-message {
      color: red;
      font-weight: bold;
      margin: 10px 0;
    }

  </style>

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
  <p class="error-message"><%= request.getAttribute("loginError") %></p>
  <% } %>
</div>
</body>
</html>

