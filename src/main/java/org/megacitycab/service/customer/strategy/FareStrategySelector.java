package org.megacitycab.service.customer.strategy;

import java.sql.Timestamp;

public class FareStrategySelector {
    public static FareCalculationStrategy getFareStrategy(Timestamp bookingDate) {
        int hour = bookingDate.toLocalDateTime().getHour();
        if ((hour >= 7 && hour < 10) || (hour >= 17 && hour < 20)) {
            return new PeakFareStrategy(); // Use peak pricing during peak hours
        } else {
            return new NormalFareStrategy(); // Use normal pricing otherwise
        }
    }
}

