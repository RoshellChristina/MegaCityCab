package org.megacitycab.dao;

import org.megacitycab.model.Admin;
import java.util.List;

public interface AdminDAO {
    void addAdmin(Admin admin);
    boolean updateAdmin(Admin admin);
    void deleteAdmin(int adminId);
    Admin getAdminById(int adminId);
    List<Admin> getAllAdmins();

        Admin getAdminByUsername(String username);


}




