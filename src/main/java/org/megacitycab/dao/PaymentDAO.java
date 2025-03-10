package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Payment;

import java.sql.*;

public class PaymentDAO {

    // Insert a payment record for a booking
    public boolean addPayment(Payment payment) {
        String sql = "INSERT INTO payment (BookingID, UserID, Amount, PaymentMethod, Status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getBookingID());
            stmt.setInt(2, payment.getUserID());
            stmt.setDouble(3, payment.getAmount());
            stmt.setString(4, payment.getPaymentMethod());
            stmt.setString(5, payment.getStatus());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update the payment status based on bookingID
    public boolean updatePaymentStatus(int bookingID, String newStatus) {
        String sql = "UPDATE payment SET Status = ? WHERE BookingID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingID);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public double getTotalRevenue(){
        double revenue = 0;
        String sql = "SELECT SUM(Amount) AS total FROM payment WHERE Status = 'Completed'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if(rs.next()){
                revenue = rs.getDouble("total");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return revenue;
    }

}


