package org.megacitycab.service.customer.strategy;

import java.sql.Timestamp;

public interface BookingEndTimeStrategy {
    Timestamp calculateEndTime(Timestamp bookingDate, double distanceKm);
}
