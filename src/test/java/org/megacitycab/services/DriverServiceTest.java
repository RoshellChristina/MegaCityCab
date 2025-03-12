package org.megacitycab.services;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.megacitycab.service.driver.BookingConflictChecker;
import org.megacitycab.service.driver.DriverService;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.*;
import org.megacitycab.model.Booking;
import org.megacitycab.model.Driver;
import org.megacitycab.model.Vehicle;
import org.megacitycab.dao.DriverDAO;
import org.megacitycab.dao.BookingDAO;
import org.megacitycab.dao.VehicleDAO;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DriverServiceTest {

    private DriverDAO driverDAOMock;
    private BookingDAO bookingDAOMock;
    private VehicleDAO vehicleDAOMock;

    private DriverService driverService;

    @Before
    public void setUp() {
        // Initialize mocks
        driverDAOMock = mock(DriverDAO.class);
        bookingDAOMock = mock(BookingDAO.class);
        vehicleDAOMock = mock(VehicleDAO.class);

        // Create the DriverService instance with mocked DAOs
        driverService = new DriverService();

        // Manually set mocked DAOs into the DriverService
        driverService.setDriverDAO(driverDAOMock);
        driverService.setBookingDAO(bookingDAOMock);
        driverService.setVehicleDAO(vehicleDAOMock);
    }

    @Test
    public void testAddDriver() {
        Driver newDriver = new Driver();
        newDriver.setName("John Doe");
        newDriver.setUsername("johndoe");
        // Hash the password before setting it
        String hashedPassword = BCrypt.hashpw("password123", BCrypt.gensalt());
        newDriver.setPasswordHash(hashedPassword);

        when(driverDAOMock.addDriver(any(Driver.class))).thenReturn(true); // Mock DAO method

        boolean result = driverService.addDriver(newDriver);

        assertTrue("Driver should be added successfully", result);
        verify(driverDAOMock).addDriver(newDriver); // Verify interaction with mock
    }

    @Test
    public void testUpdateDriver() {
        Driver existingDriver = new Driver();
        existingDriver.setDriverID(1);
        existingDriver.setName("John Doe");

        when(driverDAOMock.updateDriver(any(Driver.class))).thenReturn(true);

        boolean result = driverService.updateDriver(existingDriver);

        assertTrue("Driver should be updated successfully", result);
        verify(driverDAOMock).updateDriver(existingDriver);
    }

    @Test
    public void testDeleteDriver() {
        int driverID = 1;

        when(driverDAOMock.deleteDriver(driverID)).thenReturn(true);

        boolean result = driverService.deleteDriver(driverID);

        assertTrue("Driver should be deleted successfully", result);
        verify(driverDAOMock).deleteDriver(driverID);
    }

    @Test
    public void testGetAllDrivers() {
        List<Driver> driverList = new ArrayList<>();
        driverList.add(new Driver());

        when(driverDAOMock.getAllDrivers()).thenReturn(driverList);

        List<Driver> result = driverService.getAllDrivers();

        assertNotNull("Drivers list should not be null", result);
        assertEquals("There should be one driver", 1, result.size());
        verify(driverDAOMock).getAllDrivers();
    }

    @Test
    public void testGetDriverById() {
        int driverID = 1;
        Driver existingDriver = new Driver();
        existingDriver.setDriverID(driverID);

        when(driverDAOMock.getDriverById(driverID)).thenReturn(existingDriver);

        Driver result = driverService.getDriverById(driverID);

        assertNotNull("Driver should be found", result);
        assertEquals("Driver ID should match", driverID, result.getDriverID());
        verify(driverDAOMock).getDriverById(driverID);
    }


    @Test
    public void testGetAcceptedBookingsForDriver() {
        int driverID = 1;
        List<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking());

        when(bookingDAOMock.getAcceptedBookingsByDriverID(driverID)).thenReturn(bookings);

        List<Booking> result = driverService.getAcceptedBookingsForDriver(driverID);

        assertNotNull("Accepted bookings should not be null", result);
        assertEquals("There should be one accepted booking", 1, result.size());
        verify(bookingDAOMock).getAcceptedBookingsByDriverID(driverID);
    }
}
