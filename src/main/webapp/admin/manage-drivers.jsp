<%@ page import="org.megacitycab.model.Driver" %>
<%@ page import="org.megacitycab.service.driver.DriverService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.service.admin.VehicleService" %>
<%@ page import="org.megacitycab.model.Vehicle" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<%

List<Vehicle> vehicles = (List<Vehicle>) request.getAttribute("vehicles");
    if (vehicles == null) {
        VehicleService vehicleService = new VehicleService();
        vehicles = vehicleService.getAllVehicles();
        request.setAttribute("vehicles", vehicles);
    }

    %>

<%
    List<Driver> drivers = (List<Driver>) request.getAttribute("drivers");
    if (drivers == null) {
%>
<p>Drivers not loaded. Please access this page through the proper channel.</p>
<%
        return;
    }
%>



<html>
<head>

    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
        }
    </style>
    <title>Manage Drivers</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Make sure jQuery is loaded before Bootstrap JS and your custom scripts -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <script>
        function confirmDelete(driverID) {
            if (confirm("Are you sure you want to delete this driver?")) {
                document.getElementById('deleteDriverID').value = driverID;
                document.getElementById('deleteForm').submit();
            }
        }

        function openEditModal(driverID, name, dutyStatus, username, email, phoneNumber, assignedVehicleID) {
            document.getElementById('editDriverID').value = driverID;
            document.getElementById('editName').value = name;
            document.getElementById('editDutyStatus').value = dutyStatus;
            document.getElementById('editUsername').value = username;
            document.getElementById('editEmail').value = email;
            document.getElementById('editPhoneNumber').value = phoneNumber;

            // Ensure the password field is blank when opening the modal
            document.getElementById('editPasswordHash').value = "";

            // Set dropdown selection for assigned vehicle
            document.getElementById('editVehicleID').value = assignedVehicleID ? assignedVehicleID : "";

            var editModal = new bootstrap.Modal(document.getElementById('editDriverModal'));
            editModal.show();
        }


        function openAssignModal(driverID) {
            document.getElementById('assignDriverID').value = driverID;
            var modal = new bootstrap.Modal(document.getElementById('assignVehicleModal'));
            modal.show();
        }

        // This function is called when the Assign button (in the table) is clicked.
        function openAssignModal(driverID) {
            // Set the hidden driverID in the modal form
            document.getElementById('assignDriverID').value = driverID;

            // Clear any existing options in the dropdown
            $('#vehicleID').empty();

            // Use AJAX to fetch the latest available vehicles
            $.ajax({
                url: '${pageContext.request.contextPath}/DriverServlet?action=fetchAvailableVehicles',
                method: 'GET',
                dataType: 'json',
                success: function(data) {
                    if (data.length === 0) {
                        $('#vehicleID').append($('<option>').val('').text('No available vehicles'));
                    } else {
                        $.each(data, function(index, vehicle) {
                            $('#vehicleID').append($('<option>').val(vehicle.vehicleID)
                                .text(vehicle.licensePlate + " - " + vehicle.color));
                        });
                    }
                },
                error: function() {
                    $('#vehicleID').append($('<option>').val('').text('Error fetching vehicles'));
                }
            });

            // Open the modal using Bootstrap's modal API
            var modalElement = document.getElementById('assignVehicleModal');
            var modal = new bootstrap.Modal(modalElement);
            modal.show();
        }

    </script>


    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/DriverServlet">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="driverID" id="deleteDriverID">
    </form>
</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container mt-4">
    <h2>Manage Drivers</h2>
    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>
    <!-- Add Driver Button -->
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addDriverModal">➕ Add New Driver</button>
    <!-- Drivers Table -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Duty Status</th>
            <th>Username</th>
            <th>Email</th>
            <th>Phone Number</th>
            <th>Date Joined</th>
            <th>Assigned Vehicle</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="driver" items="${drivers}">
            <tr>
                <td>${driver.driverID}</td>
                <td>${driver.name}</td>
                <td>${driver.dutyStatus}</td>
                <td>${driver.username}</td>
                <td>${driver.email}</td>
                <td>${driver.phoneNumber}</td>
                <td>${driver.dateJoined}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty driver.assignedVehicleID}">
                            ${driver.assignedVehicleID}
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-info btn-sm" onclick="openAssignModal('${driver.driverID}')">Assign</button>
                        </c:otherwise>
                    </c:choose>
                </td>

                <td>
                    <button class="btn btn-warning btn-sm"
                            onclick="openEditModal('${driver.driverID}', '${driver.name}', '${driver.dutyStatus}', '${driver.username}', '${driver.email}', '${driver.phoneNumber}', '${driver.passwordHash}', '${driver.assignedVehicleID}')">
                        📝 Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${driver.driverID})">🗑️</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Edit Driver Modal -->
<div class="modal fade" id="editDriverModal" tabindex="-1" aria-labelledby="editDriverModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Driver</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/DriverServlet" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="editName" class="form-label">Name:</label>
                        <input type="text" id="editName" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editDutyStatus" class="form-label">Duty Status:</label>
                        <select id="editDutyStatus" name="dutyStatus" class="form-select" required>
                            <option value="On Duty">On Duty</option>
                            <option value="Off Duty">Off Duty</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="editUsername" class="form-label">Username:</label>
                        <input type="text" id="editUsername" name="username" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editEmail" class="form-label">Email:</label>
                        <input type="email" id="editEmail" name="email" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editPhoneNumber" class="form-label">Phone Number:</label>
                        <input type="text" id="editPhoneNumber" name="phoneNumber" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editPasswordHash" class="form-label">Password (Leave blank to keep current password):</label>
                        <input type="password" id="editPasswordHash" name="passwordHash" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editImage" class="form-label">Profile Image:</label>
                        <input type="file" id="editImage" name="image" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editVehicleID" class="form-label">Assign Vehicle:</label>
                        <select id="editVehicleID" name="vehicleID" class="form-select">
                            <option value="">-- None --</option>
                            <c:forEach var="veh" items="${availableVehicles}">
                                <option value="${veh.vehicleID}"
                                        <c:if test="${veh.vehicleID == driver.assignedVehicleID}">selected</c:if>>
                                        ${veh.licensePlate} (${veh.color})
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <input type="hidden" id="editDriverID" name="driverID">
                    <input type="hidden" name="action" value="edit">
                    <button type="submit" class="btn btn-primary">Update Driver</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Add Driver Modal -->
<div class="modal fade" id="addDriverModal" tabindex="-1" aria-labelledby="addDriverModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add Driver</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/DriverServlet" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name:</label>
                        <input type="text" id="name" name="name" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="dutyStatus" class="form-label">Duty Status:</label>
                        <select id="dutyStatus" name="dutyStatus" class="form-select" required>
                            <option value="On Duty">On Duty</option>
                            <option value="Off Duty">Off Duty</option>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label for="username" class="form-label">Username:</label>
                        <input type="text" id="username" name="username" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email:</label>
                        <input type="email" id="email" name="email" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="phoneNumber" class="form-label">Phone Number:</label>
                        <input type="text" id="phoneNumber" name="phoneNumber" class="form-control">
                    </div>
                    <!-- Update the name to "password" so that servlet finds it -->
                    <div class="mb-3">
                        <label for="password" class="form-label">Password:</label>
                        <input type="password" id="password" name="password" class="form-control" required>
                    </div>
                    <!-- Update the name to "imageFile" to match servlet code -->
                    <div class="mb-3">
                        <label for="imageFile" class="form-label">Profile Image:</label>
                        <input type="file" id="imageFile" name="imageFile" class="form-control">
                    </div>
                    <input type="hidden" name="action" value="insert">
                    <button type="submit" class="btn btn-success">Add Driver</button>
                </form>
            </div>
        </div>
    </div>
</div>



<!-- Assign Vehicle Modal -->
<div class="modal fade" id="assignVehicleModal" tabindex="-1" aria-labelledby="assignVehicleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="assignVehicleModalLabel">Assign Vehicle</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/DriverServlet" method="post">
                    <div class="mb-3">
                        <label for="vehicleID" class="form-label">Select Vehicle:</label>
                        <select name="vehicleID" id="vehicleID" class="form-select">
                            <!-- Options will be injected here via AJAX -->
                        </select>
                    </div>
                    <input type="hidden" name="driverID" id="assignDriverID">
                    <input type="hidden" name="action" value="assign">
                    <button type="submit" class="btn btn-primary">Assign Vehicle</button>
                </form>
            </div>
        </div>
    </div>
</div>


<%@ include file="admin-footer.jsp" %>
</body>
</html>


