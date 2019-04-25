package dao.entity;

import dao.*;

public interface EntityService {

    ListenableFuture<String> fetchEntityNameAsync(TenantId tenantId, EntityId entityId);

    void deleteEntityRelations(TenantId tenantId, EntityId entityId);

}
