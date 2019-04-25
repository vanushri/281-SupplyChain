package dao.tenant;

import com.google.common.util.concurrent.ListenableFuture;
import dao.*;

public interface TenantService {

    Tenant findTenantById(TenantId tenantId);

    ListenableFuture<Tenant> findTenantByIdAsync(TenantId callerId, TenantId tenantId);
    
    Tenant saveTenant(Tenant tenant);
    
    void deleteTenant(TenantId tenantId);
    
    TextPageData<Tenant> findTenants(TextPageLink pageLink);
    
    void deleteTenants();
}
