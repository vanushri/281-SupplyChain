package dao.dashboard;

import dao.*;

public class CassandraDashboardDao extends CassandraAbstractSearchTextDao<DashboardEntity, Dashboard> implements DashboardDao {

    @Override
    protected Class<DashboardEntity> getColumnFamilyClass() {
        return DashboardEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return DASHBOARD_COLUMN_FAMILY_NAME;
    }
}
