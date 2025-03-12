package org.megacitycab.dao;

import org.megacitycab.config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class AdminDashboardDAO {

    public double getTotalRevenue() throws SQLException {
        String sql = "SELECT SUM(Amount) AS totalRevenue FROM payment WHERE Status = 'Completed'";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        double revenue = 0;
        if(rs.next()){
            revenue = rs.getDouble("totalRevenue");
        }
        rs.close();
        ps.close();
        return revenue;
    }

    public int getBookingCountByStatus(String status) throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM booking WHERE Status = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, status);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        if(rs.next()){
            count = rs.getInt("count");
        }
        rs.close();
        ps.close();
        return count;
    }

    public Map<String, Double> getMonthlyRevenue() throws SQLException {
        String sql = "SELECT MONTHNAME(PaymentDate) AS month, SUM(Amount) AS total FROM payment WHERE Status = 'Completed' GROUP BY month ORDER BY MONTH(PaymentDate)";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        Map<String, Double> monthlyRevenue = new HashMap<>();
        while (rs.next()) {
            monthlyRevenue.put(rs.getString("month"), rs.getDouble("total"));
        }
        rs.close();
        ps.close();
        return monthlyRevenue;
    }

    public String getMostDemandedVehicleCategory() throws SQLException {
        String sql = "SELECT vc.CategoryName, COUNT(b.BookingID) AS count FROM booking b " +
                "JOIN vehiclecategory vc ON b.VehicleCategoryID = vc.CategoryID " + // Fix the join condition
                "GROUP BY vc.CategoryName " +
                "ORDER BY count DESC LIMIT 1";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        String mostDemandedCategory = "Unknown";
        if(rs.next()){
            mostDemandedCategory = rs.getString("CategoryName");
        }
        rs.close();
        ps.close();
        return mostDemandedCategory;
    }


    public int getTotalDrivers() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM driver";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        if(rs.next()){
            count = rs.getInt("count");
        }
        rs.close();
        ps.close();
        return count;
    }

    public int getTotalVehicles() throws SQLException {
        String sql = "SELECT COUNT(*) AS count FROM vehicle";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        int count = 0;
        if(rs.next()){
            count = rs.getInt("count");
        }
        rs.close();
        ps.close();
        return count;
    }

    public double getRevenueByMonth(String month) throws SQLException {
        String sql = "SELECT SUM(Amount) AS revenue FROM payment WHERE Status = 'Completed' AND MONTHNAME(PaymentDate) = ?";
        Connection conn = DBConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, month);
        ResultSet rs = ps.executeQuery();
        double revenue = 0;
        if(rs.next()){
            revenue = rs.getDouble("revenue");
        }
        rs.close();
        ps.close();
        return revenue;
    }

}
