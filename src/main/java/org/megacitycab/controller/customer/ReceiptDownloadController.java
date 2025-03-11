package org.megacitycab.controller.customer;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.megacitycab.service.customer.ReceiptService;

import java.io.IOException;

@WebServlet("/downloadReceipt")
public class ReceiptDownloadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int bookingID = Integer.parseInt(request.getParameter("bookingID"));

        // Generate PDF receipt bytes using your PDF generation service (example below)
        byte[] pdfBytes = ReceiptService.generateReceipt(bookingID);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=receipt_" + bookingID + ".pdf");
        response.setContentLength(pdfBytes.length);
        response.getOutputStream().write(pdfBytes);
    }
}

