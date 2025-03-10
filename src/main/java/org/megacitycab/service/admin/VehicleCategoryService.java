package org.megacitycab.service.admin;

import org.megacitycab.dao.VehicleCategoryDAO;
import org.megacitycab.model.VehicleCategory;

import java.util.List;

public class VehicleCategoryService {
    private static final VehicleCategoryDAO categoryDAO = new VehicleCategoryDAO();

    public boolean addCategory(String name, byte[] imageData, double price) {
        VehicleCategory category = new VehicleCategory();
        category.setCategoryName(name);
        category.setImageData(imageData);
        category.setPrice(price);

        return categoryDAO.addCategory(category);
    }

    public static List<VehicleCategory> getAllCategories() {
        return categoryDAO.getAllCategories();
    }

    public boolean updateCategory(int id, String name, byte[] imageData, double price) {
        VehicleCategory category = new VehicleCategory();
        category.setCategoryID(id);
        category.setCategoryName(name);
        category.setImageData(imageData);
        category.setPrice(price);

        return categoryDAO.updateCategory(category);
    }

    public boolean deleteCategory(int id) {
        return categoryDAO.deleteCategory(id);
    }

    public VehicleCategory getCategoryById(int categoryID) {
        return categoryDAO.getCategoryById(categoryID);
    }
}
