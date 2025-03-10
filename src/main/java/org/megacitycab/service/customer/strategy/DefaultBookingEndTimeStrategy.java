package org.megacitycab.service.customer.strategy;

import java.sql.Timestamp;

public class DefaultBookingEndTimeStrategy implements BookingEndTimeStrategy {
    @Override
    public Timestamp calculateEndTime(Timestamp bookingDate, double distanceKm) {
        // Calculate duration in seconds based on average speed (40 km/h)
        // duration (in hours) = distanceKm / 40, convert hours to seconds => *3600
        long durationSeconds = (long) ((distanceKm / 40.0) * 3600);
        long endTimeMillis = bookingDate.getTime() + (durationSeconds * 1000);
        return new Timestamp(endTimeMillis);
    }
}
