package org.megacitycab.dao;


import org.megacitycab.config.DBConnection;
import org.megacitycab.model.Admin;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAO {

    @Override
    public void addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (ImageData, Username, Name, Email, PasswordHash) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBytes(1, admin.getImageData());
            stmt.setString(2, admin.getUsername());
            stmt.setString(3, admin.getName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPasswordHash());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAdmin(int adminId) {
        String sql = "DELETE FROM admin WHERE AdminID=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Admin getAdminById(int adminId) {
        String sql = "SELECT * FROM admin WHERE AdminID=?";
        Admin admin = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = extractAdminFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public List<Admin> getAllAdmins() {
        String sql = "SELECT * FROM admin";
        List<Admin> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Admin admin = extractAdminFromResultSet(rs);
                list.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getInt("AdminID"));
        admin.setImageData(rs.getBytes("ImageData"));
        admin.setUsername(rs.getString("Username"));
        admin.setName(rs.getString("Name"));
        admin.setEmail(rs.getString("Email"));
        admin.setPasswordHash(rs.getString("PasswordHash"));
        admin.setDateRegistered(rs.getTimestamp("DateRegistered"));
        return admin;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE Username = ?";
        Admin admin = null;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    admin = extractAdminFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        String sql = "UPDATE admin SET Username = ?, Email = ?, PasswordHash = ? WHERE AdminID = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getEmail());
            stmt.setString(3, admin.getPasswordHash());
            stmt.setInt(4, admin.getAdminId());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
