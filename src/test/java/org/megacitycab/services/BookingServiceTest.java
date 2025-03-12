package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.service.customer.BookingService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.megacitycab.dao.BookingDAO;
import org.megacitycab.model.Booking;
import org.megacitycab.model.VehicleCategory;
import org.megacitycab.service.admin.VehicleCategoryService;
import org.megacitycab.service.customer.strategy.BookingEndTimeStrategy;
import org.megacitycab.service.customer.strategy.DefaultBookingEndTimeStrategy;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    private BookingService bookingService;

    @Mock
    private BookingDAO bookingDAOMock;

    @Mock
    private VehicleCategoryService vehicleCategoryServiceMock;

    @Mock
    private BookingEndTimeStrategy bookingEndTimeStrategyMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService();

        // Inject bookingDAO mock
        Field bookingDAOField = bookingService.getClass().getDeclaredField("bookingDAO");
        bookingDAOField.setAccessible(true);
        bookingDAOField.set(bookingService, bookingDAOMock);

        // Inject vehicleCategoryService mock
        Field vehicleCategoryServiceField = bookingService.getClass().getDeclaredField("vehicleCategoryService");
        vehicleCategoryServiceField.setAccessible(true);
        vehicleCategoryServiceField.set(bookingService, vehicleCategoryServiceMock);

        // Inject bookingEndTimeStrategy mock
        Field endTimeStrategyField = bookingService.getClass().getDeclaredField("endTimeStrategy");
        endTimeStrategyField.setAccessible(true);
        endTimeStrategyField.set(bookingService, bookingEndTimeStrategyMock);
    }

    @Test
    public void testCreateBooking_tooSoon() {
        int userId = 1;
        int vehicleCategoryID = 10;
        String pickupAddress = "123 Main St";
        String dropoffAddress = "456 Elm St";
        // Set bookingDate less than 30 minutes in the future
        Timestamp bookingDate = Timestamp.from(Instant.now().plus(Duration.ofMinutes(10)));
        double distanceKm = 5.0;

        // Expect a special error code (-2) when booking is too soon.
        int bookingID = bookingService.createBooking(userId, vehicleCategoryID, pickupAddress, dropoffAddress, bookingDate, distanceKm);
        assertEquals(-2, bookingID);

        // Verify no further calls to dependencies were made.
        verifyNoInteractions(bookingDAOMock, bookingEndTimeStrategyMock);

    }

    @Test
    public void testCreateBooking_invalidVehicleCategory() {
        int userId = 1;
        int vehicleCategoryID = 10;
        String pickupAddress = "123 Main St";
        String dropoffAddress = "456 Elm St";
        // Set bookingDate at least 30 minutes in the future
        Timestamp bookingDate = Timestamp.from(Instant.now().plus(Duration.ofMinutes(40)));
        double distanceKm = 5.0;

        // Simulate vehicle category not found
        when(vehicleCategoryServiceMock.getCategoryById(vehicleCategoryID)).thenReturn(null);

        // Expect error code (-1) for invalid vehicle category
        int bookingID = bookingService.createBooking(userId, vehicleCategoryID, pickupAddress, dropoffAddress, bookingDate, distanceKm);
        assertEquals(-1, bookingID);

        verify(vehicleCategoryServiceMock, times(1)).getCategoryById(vehicleCategoryID);
        verifyNoInteractions(bookingDAOMock, bookingEndTimeStrategyMock);

    }

    @Test
    public void testCreateBooking_success() {
        int userId = 1;
        int vehicleCategoryID = 10;
        String pickupAddress = "123 Main St";
        String dropoffAddress = "456 Elm St";
        Timestamp bookingDate = Timestamp.from(Instant.now().plus(Duration.ofMinutes(40)));
        double distanceKm = 5.0;
        int generatedBookingID = 100;

        // Prepare a dummy vehicle category
        VehicleCategory category = new VehicleCategory();
        when(vehicleCategoryServiceMock.getCategoryById(vehicleCategoryID)).thenReturn(category);

        // Simulate booking end time calculation
        Timestamp bookingEndTime = Timestamp.from(bookingDate.toInstant().plus(Duration.ofMinutes(15)));
        when(bookingEndTimeStrategyMock.calculateEndTime(bookingDate, distanceKm)).thenReturn(bookingEndTime);

        // Simulate a successful DAO operation
        when(bookingDAOMock.addBooking(any(Booking.class))).thenReturn(generatedBookingID);

        int bookingID = bookingService.createBooking(userId, vehicleCategoryID, pickupAddress, dropoffAddress, bookingDate, distanceKm);
        assertEquals(generatedBookingID, bookingID);

        // Capture the Booking object passed to the DAO
        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingDAOMock, times(1)).addBooking(bookingCaptor.capture());
        Booking capturedBooking = bookingCaptor.getValue();

        assertEquals(userId, capturedBooking.getUserID());
        assertEquals(vehicleCategoryID, capturedBooking.getVehicleCategoryID());
        assertEquals(pickupAddress, capturedBooking.getPickupAddress());
        assertEquals(dropoffAddress, capturedBooking.getDropoffAddress());
        assertEquals(bookingDate, capturedBooking.getBookingDate());
        assertEquals(distanceKm, capturedBooking.getDistanceKm(), 0.001);
        assertEquals("Pending", capturedBooking.getStatus());
        assertEquals(bookingEndTime, capturedBooking.getBookingEndTime());

        verify(vehicleCategoryServiceMock, times(1)).getCategoryById(vehicleCategoryID);
        verify(bookingEndTimeStrategyMock, times(1)).calculateEndTime(bookingDate, distanceKm);

        // Note: The registration with BookingNotificationManager is a static call.
        // You can consider using a spying framework if you need to verify that behavior.
    }

    @Test
    public void testCancelBooking() {
        int bookingID = 200;
        when(bookingDAOMock.updateBookingStatus(bookingID, "Cancelled")).thenReturn(true);

        boolean result = bookingService.cancelBooking(bookingID);
        assertTrue(result);
        verify(bookingDAOMock, times(1)).updateBookingStatus(bookingID, "Cancelled");
    }

    @Test
    public void testMarkBookingPaid() {
        int bookingID = 300;
        when(bookingDAOMock.updateBookingStatus(bookingID, "Paid")).thenReturn(true);

        boolean result = bookingService.markBookingPaid(bookingID);
        assertTrue(result);
        verify(bookingDAOMock, times(1)).updateBookingStatus(bookingID, "Paid");
    }

    @Test
    public void testGetBookingsByUserId() {
        int userId = 1;
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking());
        when(bookingDAOMock.getBookingsByUserId(userId)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByUserId(userId);
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(bookingDAOMock, times(1)).getBookingsByUserId(userId);
    }

    @Test
    public void testGetBookingById() {
        int bookingID = 400;
        Booking booking = new Booking();
        booking.setBookingID(bookingID);
        when(bookingDAOMock.getBookingById(bookingID)).thenReturn(booking);

        Booking result = bookingService.getBookingById(bookingID);
        assertNotNull(result);
        assertEquals(bookingID, result.getBookingID());
        verify(bookingDAOMock, times(1)).getBookingById(bookingID);
    }

    @Test
    public void testGetBookingDetailsWithDriverAndVehicle() {
        int bookingID = 500;
        Booking booking = new Booking();
        booking.setBookingID(bookingID);
        when(bookingDAOMock.getBookingDetailsWithDriverAndVehicle(bookingID)).thenReturn(booking);

        Booking result = bookingService.getBookingDetailsWithDriverAndVehicle(bookingID);
        assertNotNull(result);
        assertEquals(bookingID, result.getBookingID());
        verify(bookingDAOMock, times(1)).getBookingDetailsWithDriverAndVehicle(bookingID);
    }

    @Test
    public void testGetBookingsByMonth() {
        String month = "2025-03";
        List<Booking> bookings = Arrays.asList(new Booking(), new Booking(), new Booking());
        when(bookingDAOMock.getBookingsByMonth(month)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookings(month);
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(bookingDAOMock, times(1)).getBookingsByMonth(month);
    }

    @Test
    public void testGetAllBookings_whenMonthIsEmpty() {
        // When month is null or empty, get all bookings.
        List<Booking> bookings = Arrays.asList(new Booking());
        when(bookingDAOMock.getAllBookings()).thenReturn(bookings);

        List<Booking> result = bookingService.getBookings("");
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(bookingDAOMock, times(1)).getAllBookings();
    }
}

