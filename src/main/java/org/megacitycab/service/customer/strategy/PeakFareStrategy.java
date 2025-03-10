package org.megacitycab.service.customer.strategy;

public class PeakFareStrategy implements FareCalculationStrategy {
    @Override
    public double calculateFare(double basePrice, double distanceKm) {
        return (basePrice * 1.2) * distanceKm; // 20% surcharge during peak hours
    }
}
