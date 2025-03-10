package org.megacitycab.service.admin.analytics;

import java.sql.SQLException;

public interface AnalyticsStrategy {
    Object calculate() throws SQLException;
}
