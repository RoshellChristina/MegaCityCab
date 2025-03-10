package org.megacitycab.service.admin;

import org.megacitycab.model.Admin;
import java.util.List;

public interface AdminService {
    void addAdmin(Admin admin);
    void updateAdmin(Admin admin);
    void deleteAdmin(int adminId);
    Admin getAdminById(int adminId);
    List<Admin> getAllAdmins();
    Admin authenticateAdmin(String username, String password);
    Admin getAdminByUsername(String username);
    boolean updateAdminProfile(Admin admin);
}
