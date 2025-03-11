<%@ page import="org.megacitycab.model.Review" %>
<%@ page import="org.megacitycab.model.Driver" %>
<%@ page import="org.megacitycab.model.Customer" %>
<%@ page import="org.megacitycab.service.driver.DriverService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.service.customer.ReviewService" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
    if (loggedInCustomer == null) {
        response.sendRedirect("customer-login.jsp");
        return;
    }

    List<Driver> driverList = (List<Driver>) request.getAttribute("driverList");
    if (driverList == null) {
        DriverService driverService = new DriverService();
        driverList = driverService.getAllDrivers();
        request.setAttribute("driverList", driverList);
    }

%>

<%
    List<org.megacitycab.model.Review> reviewList = (List<org.megacitycab.model.Review>) request.getAttribute("reviewList");
    if (reviewList == null) {
        org.megacitycab.service.customer.ReviewService reviewService = new org.megacitycab.service.customer.ReviewService();
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID != null) {
            reviewList = reviewService.getReviewsByUserId(customerID);
            request.setAttribute("reviewList", reviewList);
        }
    }
%>

<html>
<head>
    <title>Manage Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>

        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
            background-attachment: fixed;
            font-family: Arial, sans-serif;
        }
        /* Star Rating CSS */
        .star-rating {
            direction: rtl;
            display: inline-block;
            font-size: 1.5em;
        }
        .star-rating input {
            display: none;
        }
        .star-rating label {
            color: #ccc;
            cursor: pointer;
        }
        .star-rating input:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label {
            color: #ffc700;
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
    <script>
        function openEditModal(reviewID, driverID, rating, comments) {
            document.getElementById('editReviewID').value = reviewID;
            document.getElementById('editDriverID').value = driverID;

            // Set the rating radio button inside the edit modal
            var editModalElem = document.getElementById('editReviewModal');
            var ratingInput = editModalElem.querySelector('input[name="rating"][value="' + rating + '"]');
            if (ratingInput) {
                ratingInput.checked = true;
            }

            document.getElementById('editComments').value = comments;
            var editModal = new bootstrap.Modal(editModalElem);
            editModal.show();
        }
        function confirmDelete(reviewID) {
            if (confirm("Are you sure you want to delete this review?")) {
                window.location.href = "${pageContext.request.contextPath}/ReviewController?action=delete&reviewID=" + reviewID;
            }
        }
    </script>
</head>
<body>
<%@ include file="cus-header.jsp" %>

<div class="container mt-4">
    <h2>Manage Reviews</h2>

    <%
        Integer customerID = (Integer) session.getAttribute("customerID");
        if (customerID == null) {
            response.sendRedirect("customer-login.jsp");
            return;
        }
    %>

    <c:if test="${not empty sessionScope.message}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
                ${sessionScope.message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        <% session.removeAttribute("message"); %>
    </c:if>

    <!-- Add Review Button -->
    <button class="btn btn-primary mb-3" data-bs-toggle="modal" data-bs-target="#addReviewModal">➕ Add New Review</button>

    <!-- Reviews Table -->
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Review ID</th>
            <th>Driver</th>
            <th>Rating</th>
            <th>Comments</th>
            <th>Review Date</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="rev" items="${reviewList}">
            <tr>
                <td>${rev.reviewID}</td>
                <td>
                    <c:forEach var="driver" items="${driverList}">
                        <c:if test="${driver.driverID == rev.driverID}">
                            ${driver.name}
                        </c:if>
                    </c:forEach>
                </td>

                <td>
                    <c:choose>
                        <c:when test="${rev.rating == 1}">★☆☆☆☆</c:when>
                        <c:when test="${rev.rating == 2}">★★☆☆☆</c:when>
                        <c:when test="${rev.rating == 3}">★★★☆☆</c:when>
                        <c:when test="${rev.rating == 4}">★★★★☆</c:when>
                        <c:when test="${rev.rating == 5}">★★★★★</c:when>
                        <c:otherwise>☆☆☆☆☆</c:otherwise>
                    </c:choose>
                </td>
                <td>${rev.comments}</td>
                <td><fmt:formatDate value="${rev.reviewDate}" pattern="yyyy-MM-dd" /></td>
                <td>
                    <button class="btn btn-warning btn-sm"
                            onclick="openEditModal('${rev.reviewID}', '${rev.driverID}', '${rev.rating}', '${rev.comments}')">
                        📝 Edit
                    </button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete(${rev.reviewID})">🗑️ Delete</button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<!-- Add Review Modal -->
<div class="modal fade" id="addReviewModal" tabindex="-1" aria-labelledby="addReviewModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addReviewModalLabel">Add Review</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/ReviewController" method="post">
                    <input type="hidden" name="action" value="add" />
                    <div class="mb-3">
                        <label for="driverID" class="form-label">Select Driver:</label>
                        <select id="driverID" name="driverID" class="form-select" required>
                            <option value="">-- Select a Driver --</option>
                            <c:forEach var="driver" items="${driverList}">
                                <option value="${driver.driverID}">${driver.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Rating:</label>
                        <div class="star-rating">
                            <input type="radio" id="star5" name="rating" value="5" required>
                            <label for="star5" title="5 stars">&#9733;</label>
                            <input type="radio" id="star4" name="rating" value="4">
                            <label for="star4" title="4 stars">&#9733;</label>
                            <input type="radio" id="star3" name="rating" value="3">
                            <label for="star3" title="3 stars">&#9733;</label>
                            <input type="radio" id="star2" name="rating" value="2">
                            <label for="star2" title="2 stars">&#9733;</label>
                            <input type="radio" id="star1" name="rating" value="1">
                            <label for="star1" title="1 star">&#9733;</label>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="comments" class="form-label">Comments:</label>
                        <textarea id="comments" name="comments" class="form-control" rows="3" placeholder="Enter your comments"></textarea>
                    </div>
                    <button type="submit" class="btn btn-success">Add Review</button>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- Edit Review Modal -->
<div class="modal fade" id="editReviewModal" tabindex="-1" aria-labelledby="editReviewModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editReviewModalLabel">Edit Review</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form action="${pageContext.request.contextPath}/ReviewController" method="post">
                    <input type="hidden" name="action" value="update" />
                    <input type="hidden" name="reviewID" id="editReviewID" />
                    <div class="mb-3">
                        <label for="editDriverID" class="form-label">Select Driver:</label>
                        <select id="editDriverID" name="driverID" class="form-select" required>
                            <option value="">-- Select a Driver --</option>
                            <c:forEach var="driver" items="${driverList}">
                                <option value="${driver.driverID}">${driver.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Rating:</label>
                        <div class="star-rating">
                            <input type="radio" id="editStar5" name="rating" value="5" required>
                            <label for="editStar5" title="5 stars">&#9733;</label>
                            <input type="radio" id="editStar4" name="rating" value="4">
                            <label for="editStar4" title="4 stars">&#9733;</label>
                            <input type="radio" id="editStar3" name="rating" value="3">
                            <label for="editStar3" title="3 stars">&#9733;</label>
                            <input type="radio" id="editStar2" name="rating" value="2">
                            <label for="editStar2" title="2 stars">&#9733;</label>
                            <input type="radio" id="editStar1" name="rating" value="1">
                            <label for="editStar1" title="1 star">&#9733;</label>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="editComments" class="form-label">Comments:</label>
                        <textarea id="editComments" name="comments" class="form-control" rows="3"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Update Review</button>
                </form>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/select2@4.0.13/dist/js/select2.min.js"></script>

<!-- Initialize Select2 on the Driver Dropdown -->
<script>
    $(document).ready(function() {
        // Initialize Select2 for the driver dropdown
        $('#driverID').select2({
            placeholder: "-- Select a Driver --",
            allowClear: true
        });
    });
</script>
<%@ include file="cus-footer.jsp" %>
</body>
</html>
