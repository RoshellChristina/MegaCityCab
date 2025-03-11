<%@ page import="org.megacitycab.model.VehicleCategory" %>
<%@ page import="org.megacitycab.service.admin.VehicleCategoryService" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.model.Review" %>
<%@ page import="org.megacitycab.service.customer.ReviewService" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    VehicleCategoryService categoryService = new VehicleCategoryService();
    List<VehicleCategory> categories = categoryService.getAllCategories();
    request.setAttribute("categories", categories);
%>
<%
    ReviewService reviewService = new ReviewService();
    List<Review> reviews = reviewService.getAllReviews();
    request.setAttribute("reviews", reviews);
%>
<html>
<head>
    <title>Customer Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        /* Global background image style */
        body {
            background-image: url("${pageContext.request.contextPath}/images/img_3.png");
            background-size: cover;
            padding-bottom: 60px;
        }
        /* Hero Section */
        .hero {
            height: 600px;
            background: rgba(0, 0, 0, 0.5) url("${pageContext.request.contextPath}/images/img_5.png") center/cover no-repeat;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
            text-shadow: 2px 2px 4px rgb(32, 36, 41);
        }
        .hero h1 {
            font-size: 3rem;
        }
        .container {
            padding-bottom: 20px;
        }
        /* Container spacing */
        .container {
            margin-bottom: 20px;
        }
        /* Horizontal scroll container */
        .scroll-container {
            overflow-x: auto;
            white-space: nowrap;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }

        /* Flex layout for scrolling items */
        .scroll-content {
            display: flex;
            gap: 20px; /* Spacing between cards */
        }

        /* Card styling */
        .vehicle-card, .review-card {
            flex: 0 0 auto; /* Prevents shrinking */
            width: 300px; /* Fixed card width */
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        /* Styling for images */
        .card-img-top {
            height: 200px;
            object-fit: cover;
            border-radius: 10px 10px 0 0;
        }

        /* Review card styling */
        .review-card {
            background: linear-gradient(90deg, rgba(41, 100, 165, 0.9), rgba(65, 105, 149, 0.6));
            color: white;
            padding: 20px;
        }

        /* Rating styles */
        .rating {
            font-size: 1.2rem;
        }

        .fas.fa-star.filled {
            color: gold;
        }

        /* Hide scrollbar but allow scrolling */
        .scroll-container::-webkit-scrollbar {
            display: none;
        }

        .scroll-container {
            -ms-overflow-style: none;  /* IE and Edge */
            scrollbar-width: none;  /* Firefox */
        }


    </style>
</head>
<body>
<%@ include file="cus-header.jsp" %>

<!-- Hero Section -->
<div class="hero">
    <h1>Welcome to Mega City Cab Service</h1>
</div>

<!-- Vehicle Categories Horizontal Scroll -->
<div class="container mt-5">
    <h2 class="text-center mb-4">Our Vehicle Categories</h2>
    <div class="scroll-container">
        <div class="scroll-content">
            <c:forEach var="category" items="${categories}">
                <div class="card vehicle-card">
                    <img src="data:image/png;base64,${category.base64Image}" alt="${category.categoryName}" class="card-img-top">
                    <div class="card-body text-center">
                        <h5 class="card-title">${category.categoryName}</h5>
                        <p class="card-text">Price: $${category.price}</p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- Customer Reviews Horizontal Scroll -->
<div class="container mt-5">
    <h2 class="text-center mb-4">Customer Reviews</h2>
    <div class="scroll-container">
        <div class="scroll-content">
            <c:forEach var="review" items="${reviews}">
                <div class="review-card p-4 text-center">
                    <h5>${review.userName}</h5>
                    <p class="rating">
                        <c:forEach begin="1" end="5" varStatus="star">
                            <i class="fas fa-star ${star.index <= review.rating ? 'filled' : ''}"></i>
                        </c:forEach>
                    </p>
                    <p class="comment">"${review.comments}"</p>
                    <small class="text-muted">Reviewed on: ${review.reviewDate}</small>
                </div>
            </c:forEach>
        </div>
    </div>
</div>


<%@ include file="cus-footer.jsp" %>
</body>
</html>
