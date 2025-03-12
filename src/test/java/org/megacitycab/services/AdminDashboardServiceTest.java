package org.megacitycab.services;

import org.megacitycab.service.admin.AdminDashboardService;
import org.megacitycab.service.admin.analytics.AnalyticsStrategy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;

public class AdminDashboardServiceTest {

    private AdminDashboardService adminDashboardService;
    private AnalyticsStrategy mockStrategy;

    @Before
    public void setUp() {
        // Create a mock instance of AnalyticsStrategy
        mockStrategy = Mockito.mock(AnalyticsStrategy.class);

        // Instantiate the service
        adminDashboardService = new AdminDashboardService();
    }

    @Test
    public void testGetAnalyticsWithBookingStatusCountStrategy() throws SQLException {
        // Define behavior of the mock strategy
        Mockito.when(mockStrategy.calculate()).thenReturn(5); // Let's assume 5 is the result of the calculation

        // Call the method
        Object result = adminDashboardService.getAnalytics(mockStrategy);

        // Assert that the result is as expected
        assertEquals(Integer.valueOf(5), result);

        // Verify that the calculate() method was called once
        Mockito.verify(mockStrategy).calculate();
    }

    @Test
    public void testGetAnalyticsWithMostDemandedVehicleCategoryStrategy() throws SQLException {
        // Define behavior of the mock strategy
        Mockito.when(mockStrategy.calculate()).thenReturn("SUV");

        // Call the method
        Object result = adminDashboardService.getAnalytics(mockStrategy);

        // Assert that the result is as expected
        assertEquals("SUV", result);

        // Verify that the calculate() method was called once
        Mockito.verify(mockStrategy).calculate();
    }

    @Test(expected = SQLException.class)
    public void testGetAnalyticsWithSQLException() throws SQLException {
        // Simulate an exception from the strategy's calculate() method
        Mockito.when(mockStrategy.calculate()).thenThrow(new SQLException("Database error"));

        // Call the method, which should throw SQLException
        adminDashboardService.getAnalytics(mockStrategy);
    }
}


