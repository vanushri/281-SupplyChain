

import java.util.List;
import java.util.Optional;

public interface ComponentDescriptorDao extends Dao<ComponentDescriptor> {

    Optional<ComponentDescriptor> saveIfNotExist(TenantId tenantId, ComponentDescriptor component);

    ComponentDescriptor findById(TenantId tenantId, ComponentDescriptorId componentId);

    ComponentDescriptor findByClazz(TenantId tenantId, String clazz);

    List<ComponentDescriptor> findByTypeAndPageLink(TenantId tenantId, ComponentType type, TextPageLink pageLink);

    List<ComponentDescriptor> findByScopeAndTypeAndPageLink(TenantId tenantId, ComponentScope scope, ComponentType type, TextPageLink pageLink);

    void deleteById(TenantId tenantId, ComponentDescriptorId componentId);

    void deleteByClazz(TenantId tenantId, String clazz);

}
