package org.megacitycab.service.admin;

import org.megacitycab.dao.AdminDAO;
import org.megacitycab.dao.AdminDAOImpl;
import org.megacitycab.model.Admin;
import org.megacitycab.util.BcryptUtil;

import java.util.List;

public class AdminServiceImpl implements AdminService {
    private AdminDAO adminDAO = new AdminDAOImpl();

    @Override
    public void addAdmin(Admin admin) {
        adminDAO.addAdmin(admin);
    }

    @Override
    public void updateAdmin(Admin admin) {
        adminDAO.updateAdmin(admin);
    }

    @Override
    public void deleteAdmin(int adminId) {
        adminDAO.deleteAdmin(adminId);
    }

    @Override
    public Admin getAdminById(int adminId) {
        return adminDAO.getAdminById(adminId);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDAO.getAllAdmins();
    }


    @Override
    public Admin authenticateAdmin(String username, String password) {
        Admin admin = adminDAO.getAdminByUsername(username);
        if (admin != null && BcryptUtil.verifyPassword(password, admin.getPasswordHash())) { // Correctly verify hashed password
            return admin;
        }
        return null;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminDAO.getAdminByUsername(username);
    }

    @Override
    public boolean updateAdminProfile(Admin admin) {
        // Update the admin profile through DAO
        return adminDAO.updateAdmin(admin);
    }

}

