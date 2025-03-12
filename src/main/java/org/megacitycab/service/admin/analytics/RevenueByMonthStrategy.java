package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;

public class RevenueByMonthStrategy implements AnalyticsStrategy {
    private String month;

    public RevenueByMonthStrategy(String month) {
        this.month = month;
    }

    @Override
    public Object calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getRevenueByMonth(month);
    }
}
