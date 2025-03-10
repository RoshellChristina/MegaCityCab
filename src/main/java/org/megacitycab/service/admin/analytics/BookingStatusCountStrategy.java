package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;

public class BookingStatusCountStrategy implements AnalyticsStrategy {
    private String status;

    public BookingStatusCountStrategy(String status) {
        this.status = status;
    }

    @Override
    public Integer calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getBookingCountByStatus(status);
    }
}
