package org.megacitycab.util;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/admin/*") // This filter applies to all URLs in the /admin/ path
public class AdminAuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Get session, but don't create a new one if it doesn't exist
        HttpSession session = httpRequest.getSession(false);

        // Allow access to the login page (or other public pages) without login
        String requestURI = httpRequest.getRequestURI();
        if (requestURI.endsWith("login.jsp")) {
            chain.doFilter(request, response); // Let the login page pass
            return;
        }

        // If session is null or adminUser is not set in session, redirect to login page
        if (session == null || session.getAttribute("adminUser") == null) {
            // No session or user is not logged in, redirect to login page
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/admin/login.jsp");
            return;
        }

        // If the user is logged in, allow the request to proceed
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
