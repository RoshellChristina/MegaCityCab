package org.megacitycab.observer;

import org.megacitycab.model.Booking;

public interface BookingObserver {
    void update(Booking booking);
}
