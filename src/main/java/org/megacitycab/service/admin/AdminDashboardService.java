package org.megacitycab.service.admin;

import org.megacitycab.service.admin.analytics.AnalyticsStrategy;
import java.sql.SQLException;

public class AdminDashboardService {

    public Object getAnalytics(AnalyticsStrategy strategy) throws SQLException {
        return strategy.calculate();
    }
}
