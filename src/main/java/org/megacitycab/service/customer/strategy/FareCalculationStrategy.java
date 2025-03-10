package org.megacitycab.service.customer.strategy;


public interface FareCalculationStrategy {
    double calculateFare(double basePrice, double distanceKm);
}
