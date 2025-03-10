<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Customer Registration</title>
    <link rel="stylesheet" href="css/style.css"> <!-- Add your CSS file here -->
</head>
<body>
<div class="container">
    <h2>Customer Registration</h2>
    <form action="${pageContext.request.contextPath}/customer" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="register">

        <label for="name">Full Name:</label>
        <input type="text" id="name" name="name" required>

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>

        <label for="nic">NIC:</label>
        <input type="text" id="nic" name="nic" required>

        <label for="phoneNumber">Phone Number:</label>
        <input type="text" id="phoneNumber" name="phoneNumber" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <label for="address">Address:</label>
        <textarea id="address" name="address" required></textarea>

        <label for="imageFile">Profile Image:</label>
        <input type="file" id="imageFile" name="imageFile" accept="image/*">

        <button type="submit">Register</button>

        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>

    </form>
    <p>Already have an account? <a href="customer-login.jsp">Login here</a></p>
</div>
</body>
</html>

