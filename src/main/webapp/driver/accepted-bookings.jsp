<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.model.Booking" %>
<%@ page import="org.megacitycab.service.driver.DriverService" %>
<%@ page import="org.megacitycab.model.Driver" %>
<%


  // Retrieve the logged-in driver from session
  Driver driver = (Driver) session.getAttribute("loggedInDriver");
  if (driver == null) {
    response.sendRedirect("driver-login.jsp");
    return;
  }
  List<Booking> acceptedBookings = (List<Booking>) request.getAttribute("acceptedBookings");

  // If acceptedBookings are not set, load them now
  if (acceptedBookings == null) {
    DriverService driverService = new DriverService();
    Integer driverID = (Integer) session.getAttribute("driverID"); // Use implicit session object

    if (driverID != null) {
      acceptedBookings = driverService.getAcceptedBookingsForDriver(driverID);
      request.setAttribute("acceptedBookings", acceptedBookings);
    }
  }
%>


<!DOCTYPE html>
<html>
<head>
  <title>Accepted Bookings</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">

  <style>
    body {
      background-image: url("<%= request.getContextPath() %>/images/img_4.png");
      background-size: cover;
      background-attachment: fixed;
    }

    .container {
      margin-top: 50px;
      max-width: 1300px;
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

    .btn-action {
      color: white;
      border: none;
      padding: 5px 15px;
      border-radius: 8px;
      transition: 0.3s;
      width: 70%; /* Full-width buttons */
      margin: 2px 0; /* Space between buttons */
    }

    .btn-cancel {
      background-color: #dc3545;
    }

    .btn-in-progress {
      background-color: #ffc107;
    }

    .btn-completed {
      background-color: #28a745;
    }

    .btn-action:hover {
      opacity: 0.8;
    }

    .alert-success {
      margin-top: 10px;
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
  </style>


</head>
<body>
<%@include file="driver-header.jsp"%>
<div class="container">
<h2>Accepted Bookings</h2>

  <c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${sessionScope.message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("message"); %>
  </c:if>
<table class="table table-striped table-bordered table-hover">
  <thead>

  <tr>
    <th>Booking ID</th>
    <th>Customer Name</th>
    <th>Pickup Address</th>
    <th>Dropoff Address</th>
    <th>Booking Date</th>
    <th>Booking EndTime</th>
    <th>Distance (km)</th>
    <th>Status</th>
  </tr>
  </thead>
  <tbody>
  <% if (acceptedBookings != null && !acceptedBookings.isEmpty()) { %>
  <% for (Booking booking : acceptedBookings) { %>
  <tr>
    <td><%= booking.getBookingID() %></td>
    <td><%= booking.getUserName() %></td>
    <td><%= booking.getPickupAddress() %></td>
    <td><%= booking.getDropoffAddress() %></td>
    <td><%= booking.getBookingDate() %></td>
    <td><%= booking.getBookingEndTime() %></td>
    <td><%= booking.getDistanceKm() %></td>
    <td><%= booking.getStatus() %></td>
    <th>Action</th>
    <td id="actions-<%= booking.getBookingID() %>">
      <!-- Action Buttons: Cancel, In Progress, Completed -->
      <form action="<%= request.getContextPath() + "/update-booking-status" %>" method="post">
        <input type="hidden" name="bookingID" value="<%= booking.getBookingID() %>">

        <!-- Cancel button -->
        <button type="submit" id="cancelled-<%= booking.getBookingID() %>" name="status" value="Cancelled"
                class="btn-action btn-cancel"
                style="<%= booking.getStatus().equals("Cancelled") || booking.getStatus().equals("Completed") ? "display:none;" : "" %>"
                onclick="toggleButtons('Cancelled', <%= booking.getBookingID() %>)">
          Cancel
        </button>

        <!-- In Progress button -->
        <button type="submit" id="inProgress-<%= booking.getBookingID() %>" name="status" value="In Progress"
                class="btn-action btn-in-progress"
                style="<%= booking.getStatus().equals("In Progress") || booking.getStatus().equals("Cancelled") || booking.getStatus().equals("Completed") ? "display:none;" : "" %>"
                onclick="toggleButtons('In Progress', <%= booking.getBookingID() %>)">
          In Progress
        </button>

        <!-- Completed button -->
        <button type="submit" id="completed-<%= booking.getBookingID() %>" name="status" value="Completed"
                class="btn-action btn-completed"
                style="<%= booking.getStatus().equals("Completed") || booking.getStatus().equals("Cancelled") ? "display:none;" : "" %>"
                onclick="toggleButtons('Completed', <%= booking.getBookingID() %>)">
          Completed
        </button>

      </form>
    </td>
  </tr>
  <% } %>
  <% } else { %>
  <tr><td colspan="5">No accepted bookings available.</td></tr>
  <% } %>
  </tbody>
</table>
</div>
<%@include file="driver-footer.jsp"
%>
</body>
</html>

