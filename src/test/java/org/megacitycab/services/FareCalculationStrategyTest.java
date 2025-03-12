package org.megacitycab.services;

import org.junit.Test;
import org.megacitycab.service.customer.strategy.FareCalculationStrategy;
import org.megacitycab.service.customer.strategy.FareStrategySelector;
import org.megacitycab.service.customer.strategy.NormalFareStrategy;
import org.megacitycab.service.customer.strategy.PeakFareStrategy;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class FareCalculationStrategyTest {

    @Test
    public void testPeakFareCalculation() {
        double basePrice = 10.0;
        double distanceKm = 5.0;
        // Expected fare with a 20% surcharge
        double expectedFare = (basePrice * 1.2) * distanceKm;

        FareCalculationStrategy strategy = new PeakFareStrategy();
        double fare = strategy.calculateFare(basePrice, distanceKm);

        assertEquals(expectedFare, fare, 0.0001);
    }

    @Test
    public void testNormalFareCalculation() {
        double basePrice = 10.0;
        double distanceKm = 5.0;
        // Expected fare without surcharge
        double expectedFare = basePrice * distanceKm;

        FareCalculationStrategy strategy = new NormalFareStrategy();
        double fare = strategy.calculateFare(basePrice, distanceKm);

        assertEquals(expectedFare, fare, 0.0001);
    }

    @Test
    public void testFareStrategySelectorForPeakMorningHours() {
        // Create a booking date at 8:00 AM (peak hour)
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 12, 8, 0);
        Timestamp bookingDate = Timestamp.valueOf(dateTime);

        FareCalculationStrategy strategy = FareStrategySelector.getFareStrategy(bookingDate);
        // Expect a PeakFareStrategy instance
        assertTrue(strategy instanceof PeakFareStrategy);
    }

    @Test
    public void testFareStrategySelectorForPeakEveningHours() {
        // Create a booking date at 6:00 PM (peak hour)
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 12, 18, 0);
        Timestamp bookingDate = Timestamp.valueOf(dateTime);

        FareCalculationStrategy strategy = FareStrategySelector.getFareStrategy(bookingDate);
        // Expect a PeakFareStrategy instance
        assertTrue(strategy instanceof PeakFareStrategy);
    }

    @Test
    public void testFareStrategySelectorForNormalHours() {
        // Create a booking date at 12:00 PM (non-peak hour)
        LocalDateTime dateTime = LocalDateTime.of(2025, 3, 12, 12, 0);
        Timestamp bookingDate = Timestamp.valueOf(dateTime);

        FareCalculationStrategy strategy = FareStrategySelector.getFareStrategy(bookingDate);
        // Expect a NormalFareStrategy instance
        assertTrue(strategy instanceof NormalFareStrategy);
    }
}
