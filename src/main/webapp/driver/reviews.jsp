<%@ page import="org.megacitycab.model.Review" %>
<%@ page import="org.megacitycab.model.Driver" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
    Driver loggedInDriver = (Driver) session.getAttribute("loggedInDriver");
    if (loggedInDriver == null) {
        response.sendRedirect("driver-login.jsp");
        return;
    }
%>

<%
    List<org.megacitycab.model.Review> reviewList = (List<org.megacitycab.model.Review>) request.getAttribute("reviewList");
    if (reviewList == null) {
        org.megacitycab.service.customer.ReviewService reviewService = new org.megacitycab.service.customer.ReviewService();
        Integer driverID = (Integer) session.getAttribute("driverID");
        if (driverID != null) {
            reviewList = reviewService.getReviewsByDriverID(driverID);
            request.setAttribute("reviewList", reviewList);
        }
    }
%>

<html>
<head>
    <title>Driver Reviews</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
        }
    </style>
</head>
<body>
<%@ include file="driver-header.jsp" %>

<div class="container mt-4">
    <h2>My Reviews</h2>

    <table class="table table-bordered">
        <thead>
        <tr>

            <th>Customer Name</th>
            <th>Rating</th>
            <th>Comments</th>
            <th>Review Date</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="rev" items="${reviewList}">
            <tr>

                <td>${rev.userName}</td>
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
                <td><fmt:formatDate value="${rev.reviewDate}" pattern="yyyy-MM-dd HH:mm" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="driver-footer.jsp" %>
</body>
</html>

