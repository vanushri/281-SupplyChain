package dao.widget;

import dao.*;

import java.util.List;
import java.util.UUID;

public interface WidgetsBundleDao extends Dao<WidgetsBundle> {

    WidgetsBundle save(TenantId tenantId, WidgetsBundle widgetsBundle);

    WidgetsBundle findWidgetsBundleByTenantIdAndAlias(UUID tenantId, String alias);

    List<WidgetsBundle> findSystemWidgetsBundles(TenantId tenantId, TextPageLink pageLink);

    List<WidgetsBundle> findTenantWidgetsBundlesByTenantId(UUID tenantId, TextPageLink pageLink);

    List<WidgetsBundle> findAllTenantWidgetsBundlesByTenantId(UUID tenantId, TextPageLink pageLink);

}

