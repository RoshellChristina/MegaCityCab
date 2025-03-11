<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.megacitycab.service.admin.VehicleCategoryService" %>
<%@ page import="org.megacitycab.model.VehicleCategory" %>
<%@ page import="java.util.List" %>

<%
    VehicleCategoryService categoryService = new VehicleCategoryService();
    List<VehicleCategory> categories = categoryService.getAllCategories();
    request.setAttribute("categories", categories);
%>

<html>
<head>
    <title>Manage Vehicle Categories</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .table-container {
            max-height: 400px;
            overflow-y: auto;
        }

         body {
             background-image: url("<%= request.getContextPath() %>/images/img_4.png");
             background-size: cover;
         }
    </style>
    <script>
        function confirmDelete(categoryID) {
            if (confirm("Are you sure you want to delete this category?")) {
                window.location.href = "${pageContext.request.contextPath}/VehicleCategoryServlet?action=delete&categoryID=" + categoryID;
            }
        }
    </script>
    <script>
        function openEditModal(categoryID, categoryName) {
            document.getElementById('editCategoryID').value = categoryID;
            document.getElementById('editCategoryName').value = categoryName;
            document.getElementById('editCategoryPrice').value = price;
            var editModal = new bootstrap.Modal(document.getElementById('editCategoryModal'));
            editModal.show();
        }
    </script>


</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container mt-4">
    <h2>Manage Vehicle Categories</h2>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>

    <c:if test="${not empty sessionScope.errorMessage}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${sessionScope.errorMessage}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("errorMessage"); %>
    </c:if>

    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addCategoryModal">➕ Add New Category</button>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>Category Name</th>
            <th>Image</th>
            <th>Rate</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="category" items="${categories}">
            <tr>
                <td>${category.categoryID}</td>
                <td>${category.categoryName}</td>
                <td>
                    <c:if test="${not empty category.base64Image}">
                        <img src="data:image/png;base64,${category.base64Image}" alt="Category Image" width="80" height="60">
                    </c:if>
                    <c:if test="${empty category.base64Image}">
                        <span>No Image</span>
                    </c:if>
                </td>
                <td>${category.price}</td>
                <td>
                    <!-- EDIT BUTTON (Opens Modal) -->
                    <button class="btn btn-warning btn-sm"
                            onclick="openEditModal('${category.categoryID}', '${category.categoryName}')">
                        📝 Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${category.categoryID})">🗑️</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Add Category Modal -->
<div class="modal fade" id="addCategoryModal" tabindex="-1" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addCategoryModalLabel">Add Vehicle Category</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/VehicleCategoryServlet" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label for="categoryName" class="form-label">Category Name:</label>
                        <input type="text" id="categoryName" name="categoryName" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="image" class="form-label">Category Image:</label>
                        <input type="file" id="image" name="image" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="price" class="form-label">Price:</label>
                        <input type="number" id="price" name="price" class="form-control" step="0.01" required>
                    </div>
                    <input type="hidden" name="action" value="add">
                    <button type="submit" class="btn btn-success">Add Category</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Edit Category Modal -->
<div class="modal fade" id="editCategoryModal" tabindex="-1" aria-labelledby="editCategoryModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editCategoryModalLabel">Edit Vehicle Category</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/VehicleCategoryServlet" method="post" enctype="multipart/form-data">
                    <input type="hidden" id="editCategoryID" name="categoryID">

                    <div class="mb-3">
                        <label for="editCategoryName" class="form-label">Category Name:</label>
                        <input type="text" id="editCategoryName" name="categoryName" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label for="editImage" class="form-label">Category Image (Optional):</label>
                        <input type="file" id="editImage" name="image" class="form-control">
                    </div>
                    <div class="mb-3">
                        <label for="editCategoryPrice" class="form-label">Price:</label>
                        <input type="number" id="editCategoryPrice" name="price" class="form-control" step="0.01" required>
                    </div>
                    <input type="hidden" name="action" value="update">
                    <button type="submit" class="btn btn-success">Update Category</button>
                </form>
            </div>
        </div>
    </div>
</div>


<%@ include file="admin-footer.jsp" %>
</body>
</html>




