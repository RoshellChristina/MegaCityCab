package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;
import java.util.Map;

public class MonthlyRevenueStrategy implements AnalyticsStrategy {

    @Override
    public Map<String, Double> calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getMonthlyRevenue();
    }
}

