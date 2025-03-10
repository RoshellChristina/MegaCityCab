package org.megacitycab.service.customer.strategy;

public class NormalFareStrategy implements FareCalculationStrategy {
    @Override
    public double calculateFare(double basePrice, double distanceKm) {
        return basePrice * distanceKm; // Standard fare calculation
    }
}
