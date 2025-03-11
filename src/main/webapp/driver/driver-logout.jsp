<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page session="true" %>

<%
    // Invalidate the current session if it exists
    HttpSession sessionObj = request.getSession(false); // Get the current session
    if (sessionObj != null) {
        sessionObj.invalidate(); // Invalidate the session
    }
    // Redirect to the login page
    response.sendRedirect("driver-login.jsp");
%>