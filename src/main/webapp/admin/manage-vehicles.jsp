<%@ page import="org.megacitycab.model.Vehicle" %>
<%@ page import="org.megacitycab.service.admin.VehicleService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.model.VehicleCategory" %>
<%@ page import="org.megacitycab.service.admin.VehicleCategoryService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  // If vehicles are not set (for example, after a redirect), load them now.
  List<Vehicle> vehicles = (List<Vehicle>) request.getAttribute("vehicles");
  if (vehicles == null) {
    VehicleService vehicleService = new VehicleService();
    vehicles = vehicleService.getAllVehicles();
    request.setAttribute("vehicles", vehicles);
  }

  // Similarly, load categories if not already set.
  List<VehicleCategory> categories = (List<VehicleCategory>) request.getAttribute("categories");
  if (categories == null) {
    VehicleCategoryService categoryService = new VehicleCategoryService();
    categories = categoryService.getAllCategories();
    request.setAttribute("categories", categories);
  }
%>

<html>
<head>
  <title>Manage Vehicles</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
  <script>
    function confirmDelete(vehicleID) {
      if (confirm("Are you sure you want to delete this vehicle?")) {
        document.getElementById('deleteVehicleID').value = vehicleID;
        document.getElementById('deleteForm').submit();
      }
    }

    function openEditModal(vehicleID, licensePlate, color, categoryID, status) {
      document.getElementById('editVehicleID').value = vehicleID;
      document.getElementById('editLicensePlate').value = licensePlate;
      document.getElementById('editColor').value = color;
      document.getElementById('editCategoryID').value = categoryID;
      document.getElementById('editStatus').value = status;
      var editModal = new bootstrap.Modal(document.getElementById('editVehicleModal'));
      editModal.show();
    }
      function toggleStatus(vehicleID) {
      if (confirm("Toggle the status of this vehicle?")) {
      window.location.href = "${pageContext.request.contextPath}/VehicleServlet?action=toggle&vehicleID=" + vehicleID;
    }
    }

  </script>
  <style>
    body {
      background-image: url("<%= request.getContextPath() %>/images/img_4.png");
      background-size: cover;
    }
  </style>
  <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/VehicleServlet">
    <input type="hidden" name="action" value="delete">
    <input type="hidden" name="vehicleID" id="deleteVehicleID">
  </form>
</head>
<body>
<!-- Optional header -->
<%@ include file="admin-header.jsp" %>

<div class="container mt-4">
  <h2>Manage Vehicles</h2>


  <c:if test="${not empty sessionScope.message}">
    <div class="alert alert-success alert-dismissible fade show" role="alert">
        ${sessionScope.message}
      <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
    <% session.removeAttribute("message"); %>
  </c:if>


  <!-- Filter by Category -->
  <form method="get" action="${pageContext.request.contextPath}/VehicleServlet" class="row g-3 mb-3">
    <input type="hidden" name="action" value="list" />
    <div class="col-auto">
      <label for="filterCategoryID" class="form-label">Filter by Category:</label>
    </div>
    <div class="col-auto">
      <select name="filterCategoryID" id="filterCategoryID" class="form-select">
        <option value="">-- All Categories --</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.categoryID}"
                  <c:if test="${selectedCategoryID == cat.categoryID}">selected</c:if>>
              ${cat.categoryName}
          </option>
        </c:forEach>
      </select>
    </div>
    <div class="col-auto">
      <button type="submit" class="btn btn-secondary">Filter</button>
    </div>
  </form>

  <!-- Add Vehicle Button -->
  <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addVehicleModal">➕ Add New Vehicle</button>

  <!-- Vehicle Table -->
  <table class="table table-bordered">
    <thead>
    <tr>
      <th>ID</th>
      <th>License Plate</th>
      <th>Color</th>
      <th>Category</th>
      <th>Status</th>
      <th>Actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="vehicle" items="${vehicles}">
      <tr>
        <td>${vehicle.vehicleID}</td>
        <td>${vehicle.licensePlate}</td>
        <td>${vehicle.color}</td>
        <!-- Access the category name via vehicle.category.categoryName -->
        <td>
          <c:choose>
            <c:when test="${vehicle.category != null}">
              ${vehicle.category.categoryName}
            </c:when>
            <c:otherwise>
              N/A
            </c:otherwise>
          </c:choose>
        </td>
        <td>${vehicle.status}</td>
        <td>
          <button class="btn btn-warning btn-sm"
                  onclick="openEditModal('${vehicle.vehicleID}', '${vehicle.licensePlate}', '${vehicle.color}', '${vehicle.categoryID}', '${vehicle.status}')">
            📝 Edit
          </button>
          <button class="btn btn-danger btn-sm" onclick="confirmDelete(${vehicle.vehicleID})">🗑️</button>
          <button class="btn btn-info btn-sm" onclick="toggleStatus(${vehicle.vehicleID})">Toggle Status</button>
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
</div>

<!-- Edit Vehicle Modal -->
<div class="modal fade" id="editVehicleModal" tabindex="-1" aria-labelledby="editVehicleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="editVehicleModalLabel">Edit Vehicle</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form action="${pageContext.request.contextPath}/VehicleServlet" method="post">
          <div class="mb-3">
            <label for="editLicensePlate" class="form-label">License Plate:</label>
            <input type="text" id="editLicensePlate" name="licensePlate" class="form-control" required>
          </div>
          <div class="mb-3">
            <label for="editColor" class="form-label">Color:</label>
            <input type="text" id="editColor" name="color" class="form-control">
          </div>
          <div class="mb-3">
            <label for="editCategoryID" class="form-label">Category:</label>
            <select id="editCategoryID" name="categoryID" class="form-select" required>
              <c:forEach var="cat" items="${categories}">
                <option value="${cat.categoryID}">${cat.categoryName}</option>
              </c:forEach>
            </select>
          </div>
          <div class="mb-3">
            <label for="editStatus" class="form-label">Status:</label>
            <input type="text" id="editStatus" name="status" class="form-control">
          </div>
          <input type="hidden" id="editVehicleID" name="vehicleID">
          <input type="hidden" name="action" value="edit">
          <button type="submit" class="btn btn-primary">Update Vehicle</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Add Vehicle Modal -->
<div class="modal fade" id="addVehicleModal" tabindex="-1" aria-labelledby="addVehicleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="addVehicleModalLabel">Add Vehicle</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <form action="${pageContext.request.contextPath}/VehicleServlet" method="post">
          <div class="mb-3">
            <label for="licensePlate" class="form-label">License Plate:</label>
            <input type="text" id="licensePlate" name="licensePlate" class="form-control" required>
          </div>
          <div class="mb-3">
            <label for="color" class="form-label">Color:</label>
            <input type="text" id="color" name="color" class="form-control">
          </div>
          <div class="mb-3">
            <label for="categoryID" class="form-label">Category:</label>
            <select id="categoryID" name="categoryID" class="form-select" required>
              <c:forEach var="cat" items="${categories}">
                <option value="${cat.categoryID}">${cat.categoryName}</option>
              </c:forEach>
            </select>
          </div>
          <input type="hidden" name="action" value="add">
          <button type="submit" class="btn btn-success">Add Vehicle</button>
        </form>
      </div>
    </div>
  </div>
</div>

<!-- Optional footer -->
<%@ include file="admin-footer.jsp" %>
</body>
</html>
