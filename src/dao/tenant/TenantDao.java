package dao.tenant;

import dao.*;

import java.util.List;

public interface TenantDao extends Dao<Tenant> {

    /**
     * Save or update tenant object
     *
     * @param tenant the tenant object
     * @return saved tenant object
     */
    Tenant save(TenantId tenantId, Tenant tenant);
    
    /**
     * Find tenants by region and page link.
     * 
     * @param region the region
     * @param pageLink the page link
     * @return the list of tenant objects
     */
    List<Tenant> findTenantsByRegion(TenantId tenantId, String region, TextPageLink pageLink);
    
}
