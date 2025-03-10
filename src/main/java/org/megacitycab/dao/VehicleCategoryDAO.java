package org.megacitycab.dao;

import org.megacitycab.model.VehicleCategory;
import org.megacitycab.config.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleCategoryDAO {

    // Add a new category with image
    public boolean addCategory(VehicleCategory category) {
        String sql = "INSERT INTO vehiclecategory (CategoryName, ImageData, Price) VALUES (?, ?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setBytes(2, category.getImageData());
            stmt.setDouble(3, category.getPrice());

            return stmt.executeUpdate() > 0; // Returns true if insertion was successful

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all categories
    public List<VehicleCategory> getAllCategories() {
        List<VehicleCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM vehiclecategory";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                VehicleCategory category = new VehicleCategory();
                category.setCategoryID(rs.getInt("CategoryID"));
                category.setCategoryName(rs.getString("CategoryName"));
                category.setImageData(rs.getBytes("ImageData"));
                category.setPrice(rs.getDouble("Price"));
                category.setCreatedDate(rs.getTimestamp("CreatedDate"));

                categories.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    // Update category with image
    public boolean updateCategory(VehicleCategory category) {
        String sql = "UPDATE vehiclecategory SET CategoryName = ?, ImageData = ?, Price = ? WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.getCategoryName());
            stmt.setBytes(2, category.getImageData());
            stmt.setDouble(3, category.getPrice());
            stmt.setInt(4, category.getCategoryID());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete a category
    public boolean deleteCategory(int id) {
        String sql = "DELETE FROM vehiclecategory WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get a single category by ID
    public VehicleCategory getCategoryById(int categoryID) {
        VehicleCategory category = null;
        String sql = "SELECT * FROM vehiclecategory WHERE CategoryID = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, categoryID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    category = new VehicleCategory();
                    category.setCategoryID(rs.getInt("CategoryID"));
                    category.setCategoryName(rs.getString("CategoryName"));
                    category.setImageData(rs.getBytes("ImageData"));
                    category.setPrice(rs.getDouble("Price"));
                    category.setCreatedDate(rs.getTimestamp("CreatedDate"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }
}
