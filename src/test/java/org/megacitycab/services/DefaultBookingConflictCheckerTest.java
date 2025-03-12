package org.megacitycab.services;

import org.junit.Before;
import org.junit.Test;
import org.megacitycab.dao.BookingDAO;
import org.megacitycab.model.Booking;
import org.megacitycab.service.driver.BookingConflictChecker;
import org.megacitycab.service.driver.DefaultBookingConflictChecker;
import org.mockito.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DefaultBookingConflictCheckerTest {

    @Mock
    private BookingDAO bookingDAOMock;

    private BookingConflictChecker conflictChecker;

    @Mock
    private Booking booking1;

    @Mock
    private Booking booking2;

    private Timestamp newBookingStart;
    private Timestamp newBookingEnd;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Initialize the conflict checker with mocked BookingDAO
        conflictChecker = new DefaultBookingConflictChecker(bookingDAOMock);

        // Set up timestamps for new booking
        newBookingStart = Timestamp.valueOf("2025-03-12 10:00:00");
        newBookingEnd = Timestamp.valueOf("2025-03-12 12:00:00");

        // Mock existing bookings
        booking1 = mock(Booking.class);
        booking2 = mock(Booking.class);

        // Set the mock bookings' start and end times
        when(booking1.getBookingDate()).thenReturn(Timestamp.valueOf("2025-03-12 08:00:00"));
        when(booking1.getBookingEndTime()).thenReturn(Timestamp.valueOf("2025-03-12 10:30:00"));

        when(booking2.getBookingDate()).thenReturn(Timestamp.valueOf("2025-03-12 13:00:00"));
        when(booking2.getBookingEndTime()).thenReturn(Timestamp.valueOf("2025-03-12 15:00:00"));

        // Return the mocked bookings when getAcceptedBookingsByDriverID is called
        when(bookingDAOMock.getAcceptedBookingsByDriverID(anyInt())).thenReturn(Arrays.asList(booking1, booking2));
    }

    @Test
    public void testHasConflict_OverlappingBooking() {
        // Test case where the new booking overlaps with an existing one
        when(booking1.getBookingDate()).thenReturn(Timestamp.valueOf("2025-03-12 08:00:00"));
        when(booking1.getBookingEndTime()).thenReturn(Timestamp.valueOf("2025-03-12 10:30:00"));

        boolean conflict = conflictChecker.hasConflict(1, newBookingStart, newBookingEnd);
        assertTrue("Conflict should be detected", conflict);
    }

    @Test
    public void testHasConflict_NoOverlap() {
        Timestamp newBookingStart = Timestamp.valueOf("2025-03-12 10:30:00");
        Timestamp newBookingEnd = Timestamp.valueOf("2025-03-12 12:00:00");

        // Existing booking: 2025-03-12 08:00:00 to 2025-03-12 10:00:00
        Timestamp existingBookingStart = Timestamp.valueOf("2025-03-12 08:00:00");
        Timestamp existingBookingEnd = Timestamp.valueOf("2025-03-12 10:00:00");

        // Setting up mock bookings for testing
        List<Booking> acceptedBookings = new ArrayList<>();
        Booking existingBooking = new Booking();
        existingBooking.setBookingDate(existingBookingStart);
        existingBooking.setBookingEndTime(existingBookingEnd);
        acceptedBookings.add(existingBooking);

        // Mock DAO behavior
        when(bookingDAOMock.getAcceptedBookingsByDriverID(anyInt())).thenReturn(acceptedBookings);

        boolean conflict = conflictChecker.hasConflict(1, newBookingStart, newBookingEnd);

        // No conflict should be detected
        assertFalse("No conflict should be detected", conflict);
    }

}
