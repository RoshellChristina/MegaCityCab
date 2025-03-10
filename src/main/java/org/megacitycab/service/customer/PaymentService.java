package org.megacitycab.service.customer;

import org.megacitycab.dao.PaymentDAO;
import org.megacitycab.model.Payment;

public class PaymentService {
    private PaymentDAO paymentDAO = new PaymentDAO();

    // Creates a payment record associated with a booking.
    public boolean createPayment(int bookingID, int userId, double amount, String paymentMethod) {
        Payment payment = new Payment();
        payment.setBookingID(bookingID);
        payment.setUserID(userId);
        payment.setAmount(amount);
        // paymentDate will default to CURRENT_TIMESTAMP in the DB if not set explicitly.
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("Completed"); // You can update this as your payment processing evolves
        return paymentDAO.addPayment(payment);
    }

    public boolean updatePaymentStatus(int bookingID, String newStatus) {
        return paymentDAO.updatePaymentStatus(bookingID, newStatus);
    }

}
