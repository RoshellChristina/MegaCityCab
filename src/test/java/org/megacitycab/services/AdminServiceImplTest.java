package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.dao.AdminDAO;
import org.megacitycab.model.Admin;
import org.megacitycab.service.admin.AdminServiceImpl;
import org.megacitycab.util.BcryptUtil;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import java.lang.reflect.Field;

public class AdminServiceImplTest {

    private AdminServiceImpl adminService;
    private AdminDAO mockAdminDAO;
    private BcryptUtil mockBcryptUtil;

    @Before
    public void setUp() throws Exception {
        // Mock AdminDAO and BcryptUtil
        mockAdminDAO = mock(AdminDAO.class);
        mockBcryptUtil = mock(BcryptUtil.class);

        // Instantiate the service
        adminService = new AdminServiceImpl();

        // Use reflection to inject the mock AdminDAO into the private field of adminService
        Field adminDAOField = AdminServiceImpl.class.getDeclaredField("adminDAO");
        adminDAOField.setAccessible(true); // Make the private field accessible
        adminDAOField.set(adminService, mockAdminDAO); // Inject the mock AdminDAO into the service
    }

    @Test
    public void testAddAdmin() {
        // Arrange
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setUsername("newAdmin");
        admin.setPasswordHash("hashedPassword");

        // Act
        adminService.addAdmin(admin);

        // Assert - Verify that the addAdmin method of AdminDAO was called once
        verify(mockAdminDAO, times(1)).addAdmin(admin);
    }

    @Test
    public void testUpdateAdmin() {
        // Arrange
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setUsername("updatedAdmin");

        // Act
        adminService.updateAdmin(admin);

        // Assert - Verify that the updateAdmin method of AdminDAO was called once
        verify(mockAdminDAO, times(1)).updateAdmin(admin);
    }

    @Test
    public void testDeleteAdmin() {
        // Arrange
        int adminId = 1;

        // Act
        adminService.deleteAdmin(adminId);

        // Assert - Verify that the deleteAdmin method of AdminDAO was called once
        verify(mockAdminDAO, times(1)).deleteAdmin(adminId);
    }

    @Test
    public void testGetAdminById() {
        // Arrange
        int adminId = 1;
        Admin mockAdmin = new Admin();
        mockAdmin.setAdminId(adminId);
        mockAdmin.setUsername("admin");

        // Set up the mock AdminDAO to return the mockAdmin
        when(mockAdminDAO.getAdminById(adminId)).thenReturn(mockAdmin);

        // Act
        Admin result = adminService.getAdminById(adminId);

        // Assert - Verify the result
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
    }


    @Test
    public void testGetAdminByUsername() {
        // Arrange
        String username = "admin";
        Admin mockAdmin = new Admin();
        mockAdmin.setUsername(username);

        // Set up the mock AdminDAO to return the mockAdmin
        when(mockAdminDAO.getAdminByUsername(username)).thenReturn(mockAdmin);

        // Act
        Admin result = adminService.getAdminByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void testUpdateAdminProfile() {
        // Arrange
        Admin admin = new Admin();
        admin.setAdminId(1);
        admin.setUsername("updatedProfile");

        // Set up the mock AdminDAO to return true when updating
        when(mockAdminDAO.updateAdmin(admin)).thenReturn(true);

        // Act
        boolean result = adminService.updateAdminProfile(admin);

        // Assert
        assertTrue(result);
        verify(mockAdminDAO, times(1)).updateAdmin(admin);
    }
}
