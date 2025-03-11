package org.megacitycab.service.driver;

import java.sql.Timestamp;

public interface BookingConflictChecker {

    boolean hasConflict(int driverID, Timestamp newBookingStart, Timestamp newBookingEnd);
}
