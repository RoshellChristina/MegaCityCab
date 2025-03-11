<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>View Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .container {
            margin-top: 20px;
        }
        .rating-filter {
            margin-bottom: 20px;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .table-container {
            max-height: 400px;
            overflow-y: auto;
        }
         body {
             background-image: url("<%= request.getContextPath() %>/images/img_4.png");
             background-size: cover;
         }
    </style>
</head>
<body>
<%@ include file="admin-header.jsp" %>
<div class="container">
    <h2>All Reviews</h2>
    <div class="rating-filter">
        <label for="ratingSelect" class="form-label">Filter by Rating:</label>
        <select id="ratingSelect" class="form-select" onchange="filterReviews()">
            <option value="" selected>All Ratings</option>
            <c:forEach var="i" begin="1" end="5">
                <option value="${i}"
                        <c:if test="${param.rating == i}">
                            selected
                        </c:if>
                >${i} Star</option>
            </c:forEach>
        </select>
    </div>
    <div class="table-responsive">
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>Review ID</th>
                <th>User ID</th>
                <th>Driver ID</th>
                <th>Rating</th>
                <th>Comments</th>
                <th>Review Date</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="review" items="${reviews}">
                <tr>
                    <td>${review.reviewID}</td>
                    <td>${review.userID}</td>
                    <td>${review.driverID}</td>
                    <td>${review.rating} ⭐</td>
                    <td>${review.comments}</td>
                    <td>${review.reviewDate}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
    function filterReviews() {
        const selectedRating = document.getElementById("ratingSelect").value;
        window.location.href = "${pageContext.request.contextPath}/ViewAllReviewsServlet?rating=" + selectedRating;
    }
</script>
<%@ include file="admin-footer.jsp" %>
</body>
</html>

