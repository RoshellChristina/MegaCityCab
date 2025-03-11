<%@ page import="org.megacitycab.model.VehicleCategory" %>
<%@ page import="org.megacitycab.service.admin.VehicleCategoryService" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  // Load categories if not already in the request scope.
  List<VehicleCategory> categories = (List<VehicleCategory>) request.getAttribute("categories");
  if (categories == null) {
    VehicleCategoryService categoryService = new VehicleCategoryService();
    categories = categoryService.getAllCategories();
    request.setAttribute("categories", categories);
  }
%>

<%
  // Fetch session and customer ID
  HttpSession cussession = request.getSession(false);
  Integer customerID = (Integer) cussession.getAttribute("customerID");

  // Check if the customer ID is null, meaning the user is not logged in
  if (customerID == null) {
    response.sendRedirect("customer-login.jsp");
    return; // Stop further execution if the user is not logged in
  }
%>
<html>
<head>
  <title>Book a Ride</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

  <style>
    body {
      background-image: url("<%= request.getContextPath() %>/images/img_4.png");
      background-size: cover;
      background-attachment: fixed;
    }

    .container {
      margin-top: 50px;
      max-width: 600px;
      background-color: rgba(255, 255, 255, 0.9);
      padding: 20px;
      border-radius: 12px;
      box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
      font-weight: bold;
    }

    table {
      width: 100%;
      margin-top: 20px;
    }

    .table thead {

      background-color: #134378;
      color: white;
      text-align: center;
      font-weight: 600;

    }

    input[type="submit"] {
      width: 100%;
      background-color: #134378;
      color: white;
      padding: 10px;
      margin-top: 10px;
      border: none;
      border-radius: 8px;
      font-size: 16px;
      cursor: pointer;
      transition: background-color 0.3s ease;
    }

    input[type="submit"]:hover {
      background-color: #0f3460;}
  </style>

</head>
<body>
<%@include file="cus-header.jsp"%>
<div class="container">
<h2>Book a Ride</h2>

<form action="${pageContext.request.contextPath}/customerbooking" method="post">
  <label for="vehicleCategoryID">Vehicle Category:</label>
  <select name="vehicleCategoryID" id="vehicleCategoryID" required>
    <option value="">Select Category</option>
    <c:forEach var="cat" items="${categories}">
      <option value="${cat.categoryID}">${cat.categoryName}</option>
    </c:forEach>
  </select><br/><br/>

  <label for="pickupAddress">Pickup Address:</label>
  <input type="text" name="pickupAddress" id="pickupAddress" required/><br/><br/>

  <label for="dropoffAddress">Dropoff Address:</label>
  <input type="text" name="dropoffAddress" id="dropoffAddress" required/><br/><br/>

  <!-- Changed text input to datetime-local -->
  <label for="bookingDate">Booking Date & Time:</label>
  <input type="datetime-local" name="bookingDate" id="bookingDate" required/><br/><br/>

  <label for="distanceKm">Distance (in Km):</label>
  <input type="number" step="0.01" name="distanceKm" id="distanceKm" required/><br/><br/>

  <div id="fareDisplay" style="margin-top:10px; font-weight: bold;"></div>

  <input type="submit" value="Book Ride"/>
</form>
</div>
<script>
  // Function to call the server to calculate the fare
  async function calculateFare() {
    const vehicleCategoryID = document.getElementById('vehicleCategoryID').value;
    const bookingDate = document.getElementById('bookingDate').value;
    const distanceKm = document.getElementById('distanceKm').value;

    // Only perform calculation if all fields are filled
    if(vehicleCategoryID && bookingDate && distanceKm) {
      const params = new URLSearchParams({
        vehicleCategoryID: vehicleCategoryID,
        bookingDate: bookingDate,
        distanceKm: distanceKm,
        action: 'calculateFare'
      });

      try {
        const response = await fetch('<%= request.getContextPath() %>/customerbooking?' + params.toString());
        if(response.ok) {
          const data = await response.json();
          document.getElementById('fareDisplay').innerText = "Estimated Fare: " + data.fare;
        } else {
          document.getElementById('fareDisplay').innerText = "Error calculating fare";
        }
      } catch (error) {
        document.getElementById('fareDisplay').innerText = "Error calculating fare";
      }
    }
  }

  // Add event listeners on fields
  document.getElementById('vehicleCategoryID').addEventListener('change', calculateFare);
  document.getElementById('bookingDate').addEventListener('change', calculateFare);
  document.getElementById('distanceKm').addEventListener('input', calculateFare);
</script>
<!-- Element to display fare estimate -->


<%@include file="cus-footer.jsp"%>
</body>
</html>

