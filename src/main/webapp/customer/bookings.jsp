<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="org.megacitycab.model.Booking" %>

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

<% if (request.getAttribute("downloadReceipt") != null) {
    int bookingID = (Integer) request.getAttribute("downloadReceipt");
%>
<script>
    window.onload = function() {
        // Trigger download by setting the window location to the download endpoint
        window.location.href = '<%= request.getContextPath() %>/downloadReceipt?bookingID=<%= bookingID %>';
    };
</script>
<% } %>

<html>
<head>
    <title>My Bookings</title>

    <style>
        body {
            background-image: url("<%= request.getContextPath() %>/images/img_4.png");
            background-size: cover;
            background-attachment: fixed;

        }

        /* Styling for the bookings container */
        .container {
            margin: 50px auto;
            max-width: 1200px;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
            text-align: center;
        }

        /* Styling for the table */
        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        .table thead {
            background-color: #134378;
            color: white;
            text-align: center;
            font-weight: 600;
        }

        table th, table td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: center;
        }

        h2 {
            margin-bottom: 20px;
            font-weight: bold;
        }

        /* Replace the global button, a styles with a class-specific rule */
        .btn {
            background-color: #32649a;
            color: white;
            padding: 8px 16px;
            border: none;
            border-radius: 6px;
            cursor: pointer;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        .btn:hover {
            background-color: #0d335d;
        }


        /* Modal styling */
        #infoModal, #paymentModal {
            display: none;
            position: fixed;
            top: 20%;
            left: 50%;
            transform: translate(-50%, -20%);
            width: 400px;
            padding: 20px;
            background-color: white;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
            z-index: 1000;
        }

        #infoModal h3, #paymentModal h3 {
            margin-bottom: 10px;
        }

        #paymentForm label, #paymentForm input, #paymentForm select {
            display: block;
            width: 100%;
            margin: 5px 0;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 6px;
        }

        #paymentForm button {
            margin-top: 10px;
        }

        #cardDetails {
            margin-top: 15px;
        }

        #infoModal button, #paymentModal button {
            margin-top: 10px;
            width: 100%;
        }

        /* Overlay background for modal */
        .modal-overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            z-index: 999;
        }
    </style>

</head>
<body>

<%@include file="cus-header.jsp"%>
<div class="container">
<h2>My Bookings</h2>


<p style="color: green;"><%= request.getAttribute("message") != null ? request.getAttribute("message") : "" %></p>
<table border="1" cellpadding="5" cellspacing="0">
    <tr>
        <th>Booking ID</th>
        <th>Vehicle Category ID</th>
        <th>Pickup Address</th>
        <th>Dropoff Address</th>
        <th>Booking Date</th>
        <th>Booking EndTime</th>
        <th>Distance (Km)</th>
        <th>Status</th>
        <th>Action</th>
    </tr>
    <%
        List<Booking> bookings = (List<Booking>) request.getAttribute("bookings");
        if (bookings != null) {
            for (Booking b : bookings) {
    %>
    <tr>
        <td><%= b.getBookingID() %></td>
        <td><%= b.getVehicleCategoryID() %></td>
        <td><%= b.getPickupAddress() %></td>
        <td><%= b.getDropoffAddress() %></td>
        <td><%= b.getBookingDate() %></td>
        <td><%= b.getBookingEndTime() %></td>
        <td><%= b.getDistanceKm() %></td>
        <td><%= b.getStatus() %></td>
        <td>
            <% if (!"Cancelled".equals(b.getStatus())) { %>
            <a href="${pageContext.request.contextPath}/customerbooking?action=cancel&bookingID=<%= b.getBookingID() %>" class="btn">Cancel</a>
            <% } else { %>
            N/A
            <% } %>
        </td>

        <td>
            <% if (!"Pending".equals(b.getStatus())) { %>
            <button class="btn" onclick="openInfoModal(<%= b.getBookingID() %>)">View Info</button>
            <% } else { %>
            N/A
            <% } %>
        </td>
        <!-- In the table's Action column -->
        <td>
            <% if ("Completed".equals(b.getStatus())) { %>
            <!-- The Pay button opens the payment modal passing the bookingID -->
            <button class="btn" onclick="openPaymentModal(<%= b.getBookingID() %>)">Pay</button>

            <% } %>
        </td>
    </tr>
    <%
            }
        }
    %>
</table>

<!-- Info Modal -->
    <div id="infoModal" class="modal">
    <h3>Booking Details</h3>
    <div id="infoModalContent"></div>  <!-- This will be updated dynamically -->
    <br/>
    <button class="btn" type="button" onclick="closeInfoModal()">Close</button>
</div>

<!-- Payment Modal (hidden by default) -->
    <div id="paymentModal" class="modal">
    <h3>Complete Payment</h3>
    <form id="paymentForm" method="post" action="${pageContext.request.contextPath}/customerbooking">
        <input type="hidden" name="action" value="pay" />
        <input type="hidden" id="bookingID" name="bookingID" value="" />

        <!-- Payment Method Selection -->
        <label for="paymentMethod">Payment Method:</label>
        <select name="paymentMethod" id="paymentMethod" required onchange="toggleCardDetails()">
            <option value="Credit Card">Credit Card</option>
            <option value="Debit Card">Debit Card</option>
            <option value="Cash">Cash</option>
        </select>

        <!-- Card Details (hidden by default) -->
        <div id="cardDetails" style="display: none; margin-top: 20px;">
            <label for="cardNumber">Card Number:</label>
            <input type="text" id="cardNumber" name="cardNumber" maxlength="16" pattern="\d{16}" placeholder="1234 5678 9012 3456" required />
            <br/><br/>

            <label for="expiryDate">Expiry Date:</label>
            <input type="month" id="expiryDate" name="expiryDate" required />
            <br/><br/>

            <label for="cvv">CVV:</label>
            <input type="text" id="cvv" name="cvv" maxlength="3" pattern="\d{3}" placeholder="123" required />
        </div>

        <br/>
        <button type="submit">Submit Payment</button>
        <button  class="btn" type="button" onclick="closePaymentModal()">Close</button>
    </form>
</div>
    <div id="modalOverlay" class="modal-overlay"></div>
<!-- JavaScript to toggle card details -->
<script>

    function openPaymentModal(bookingID) {
        document.getElementById("bookingID").value = bookingID;
        document.getElementById("paymentModal").style.display = "block";
    }

    function toggleCardDetails() {
        const paymentMethod = document.getElementById('paymentMethod').value;
        const cardDetails = document.getElementById('cardDetails');
        const inputs = cardDetails.querySelectorAll('input');

        if (paymentMethod === 'Credit Card' || paymentMethod === 'Debit Card') {
            cardDetails.style.display = 'block';
            inputs.forEach(input => input.required = true);
        } else {
            cardDetails.style.display = 'none';
            inputs.forEach(input => input.required = false);
        }
    }

    function closePaymentModal() {
        document.getElementById('paymentModal').style.display = 'none';
    }
</script>

<script>

    function openInfoModal(bookingID) {
        fetch('${pageContext.request.contextPath}/customerbooking?action=viewDetails&bookingID=' + bookingID)
            .then(response => response.text())  // Expecting HTML response
            .then(data => {
                document.getElementById("infoModalContent").innerHTML = data;  // Insert response into modal
                document.getElementById("infoModal").style.display = "block";  // Show modal
            })
            .catch(error => console.error('Error fetching details:', error));
    }


    function closeInfoModal() {
        document.getElementById("infoModal").style.display = "none";
    }
</script>

<br/>
</div>
<%@include file="cus-footer.jsp"%>
</body>
</html>


