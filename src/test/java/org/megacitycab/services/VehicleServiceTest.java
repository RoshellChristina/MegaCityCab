package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.dao.VehicleDAO;
import org.megacitycab.model.Vehicle;
import org.megacitycab.service.admin.VehicleService;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VehicleServiceTest {

    private VehicleService vehicleService;
    private VehicleDAO mockVehicleDAO;

    @Before
    public void setUp() {
        mockVehicleDAO = mock(VehicleDAO.class);
        vehicleService = new VehicleService();
        // Use reflection to set the private field
        try {
            java.lang.reflect.Field daoField = VehicleService.class.getDeclaredField("vehicleDAO");
            daoField.setAccessible(true);
            daoField.set(vehicleService, mockVehicleDAO);
        } catch (Exception e) {
            fail("Failed to inject mock DAO: " + e.getMessage());
        }
    }

    @Test
    public void testAddVehicle() {
        Vehicle vehicle = new Vehicle();
        when(mockVehicleDAO.addVehicle(vehicle)).thenReturn(true);

        boolean result = vehicleService.addVehicle(vehicle);
        assertTrue("Vehicle should be added successfully", result);

        verify(mockVehicleDAO, times(1)).addVehicle(vehicle);
    }

    @Test
    public void testUpdateVehicle() {
        Vehicle vehicle = new Vehicle();
        when(mockVehicleDAO.updateVehicle(vehicle)).thenReturn(true);

        boolean result = vehicleService.updateVehicle(vehicle);
        assertTrue("Vehicle should be updated successfully", result);

        verify(mockVehicleDAO, times(1)).updateVehicle(vehicle);
    }

    @Test
    public void testDeleteVehicle() {
        int vehicleID = 1;
        when(mockVehicleDAO.deleteVehicle(vehicleID)).thenReturn(true);

        boolean result = vehicleService.deleteVehicle(vehicleID);
        assertTrue("Vehicle should be deleted successfully", result);

        verify(mockVehicleDAO, times(1)).deleteVehicle(vehicleID);
    }

    @Test
    public void testGetAllVehicles() {
        List<Vehicle> mockVehicles = mock(List.class);
        when(mockVehicleDAO.getAllVehicles()).thenReturn(mockVehicles);

        List<Vehicle> result = vehicleService.getAllVehicles();
        assertNotNull("Vehicle list should not be null", result);

        verify(mockVehicleDAO, times(1)).getAllVehicles();
    }

    @Test
    public void testGetVehicleById() {
        int vehicleID = 1;
        Vehicle vehicle = new Vehicle();
        when(mockVehicleDAO.getVehicleById(vehicleID)).thenReturn(vehicle);

        Vehicle result = vehicleService.getVehicleById(vehicleID);
        assertNotNull("Vehicle should not be null", result);

        verify(mockVehicleDAO, times(1)).getVehicleById(vehicleID);
    }
}
