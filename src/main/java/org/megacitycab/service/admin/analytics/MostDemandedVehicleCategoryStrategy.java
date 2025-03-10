package org.megacitycab.service.admin.analytics;

import org.megacitycab.dao.AdminDashboardDAO;
import java.sql.SQLException;

public class MostDemandedVehicleCategoryStrategy implements AnalyticsStrategy {

    @Override
    public String calculate() throws SQLException {
        AdminDashboardDAO dao = new AdminDashboardDAO();
        return dao.getMostDemandedVehicleCategory();
    }
}
