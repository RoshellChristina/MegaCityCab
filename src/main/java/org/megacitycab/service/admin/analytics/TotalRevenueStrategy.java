package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;

public class TotalRevenueStrategy implements AnalyticsStrategy {

    @Override
    public Double calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getTotalRevenue();
    }
}
