<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Admin Login</title>
  <style>
    body {
      font-family: 'Arial', sans-serif;
      background-image: url('<%= request.getContextPath() %>/images/img_1.png');/* Set your background image here */
      background-size: cover;
      margin: 0;
      padding: 0;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }

    .container {

      background-color: rgba(255, 255, 255, 0.9); /* Slight transparency for a modern look */
      padding: 30px;
      border-radius: 8px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      width: 100%;
      max-width: 500px; /* Made the container wider */
      text-align: center;
    }

    h2 {
      font-size: 26px; /* Increased font size for a more professional look */
      color: #333;
      margin-bottom: 20px;
      font-weight: 600;
    }

    label {
      display: block;
      text-align: left;
      font-size: 14px;
      margin-bottom: 8px;
      color: #555;
    }

    input[type="text"], input[type="password"] {
      width: 100%;
      padding: 12px;
      margin: 10px 0;
      border-radius: 6px;
      border: 1px solid #ddd;
      font-size: 14px;
      box-sizing: border-box;
    }

    input[type="text"]:focus, input[type="password"]:focus {
      border-color: #4CAF50;
      outline: none;
    }

    button {
      width: 100%;
      padding: 14px;
      background-color: #4CAF50;
      color: white;
      border: none;
      border-radius: 6px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s;
    }

    button:hover {
      background-color: #45a049;
    }

    .error-message {
      color: red;
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
<div class="container">
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
</div>
</body>
</html>
