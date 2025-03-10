package org.megacitycab.dao;


import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    @Override
    public int getUserCount() {
        String sql = "SELECT COUNT(*) AS totalUsers FROM user";
        int userCount = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                userCount = rs.getInt("totalUsers");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userCount;
    }


    @Override
    public void addCustomer(Customer customer) {
        String sql = "INSERT INTO user (ImageData, Name, Username, Email, NIC, PhoneNumber, PasswordHash, Address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getImageData());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getUsername());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getNic());
            stmt.setString(6, customer.getPhoneNumber());
            stmt.setString(7, customer.getPasswordHash());
            stmt.setString(8, customer.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public Customer getCustomerByUsername(String username) {
        String sql = "SELECT * FROM user WHERE Username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setUserId(rs.getInt("UserID"));
                customer.setImageData(rs.getString("ImageData"));
                customer.setName(rs.getString("Name"));
                customer.setUsername(rs.getString("Username"));
                customer.setEmail(rs.getString("Email"));
                customer.setNic(rs.getString("NIC"));
                customer.setPhoneNumber(rs.getString("PhoneNumber"));
                customer.setPasswordHash(rs.getString("PasswordHash"));
                customer.setAddress(rs.getString("Address"));
                return customer;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void updateCustomer(Customer customer) {
        String sql = "UPDATE user SET ImageData=?, Name=?, Username=?, Email=?, NIC=?, PhoneNumber=?, PasswordHash=?, Address=? WHERE UserID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getImageData());
            stmt.setString(2, customer.getName());
            stmt.setString(3, customer.getUsername());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getNic());
            stmt.setString(6, customer.getPhoneNumber());
            stmt.setString(7, customer.getPasswordHash());
            stmt.setString(8, customer.getAddress());
            stmt.setInt(9, customer.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        String sql = "DELETE FROM user WHERE UserID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {
        String sql = "SELECT * FROM user WHERE UserID=?";
        Customer customer = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    customer = extractCustomerFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
        String sql = "SELECT * FROM user";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer customer = extractCustomerFromResultSet(rs);
                list.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Customer extractCustomerFromResultSet(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setUserId(rs.getInt("UserID"));
        customer.setImageData(rs.getString("ImageData"));
        customer.setName(rs.getString("Name"));
        customer.setUsername(rs.getString("Username"));
        customer.setEmail(rs.getString("Email"));
        customer.setNic(rs.getString("NIC"));
        customer.setPhoneNumber(rs.getString("PhoneNumber"));
        customer.setPasswordHash(rs.getString("PasswordHash"));
        customer.setAddress(rs.getString("Address"));
        customer.setDateRegistered(rs.getTimestamp("DateRegistered"));
        return customer;
    }


}
