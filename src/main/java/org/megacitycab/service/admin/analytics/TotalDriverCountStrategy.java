package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;

public class TotalDriverCountStrategy implements AnalyticsStrategy {

    @Override
    public Integer calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getTotalDrivers();
    }
}
