public interface ComponentDescriptorService {

    ComponentDescriptor saveComponent(TenantId tenantId, ComponentDescriptor component);

    ComponentDescriptor findById(TenantId tenantId, ComponentDescriptorId componentId);

    ComponentDescriptor findByClazz(TenantId tenantId, String clazz);

    TextPageData<ComponentDescriptor> findByTypeAndPageLink(TenantId tenantId, ComponentType type, TextPageLink pageLink);

    TextPageData<ComponentDescriptor> findByScopeAndTypeAndPageLink(TenantId tenantId, ComponentScope scope, ComponentType type, TextPageLink pageLink);

    boolean validate(TenantId tenantId, ComponentDescriptor component, JsonNode configuration);

    void deleteByClazz(TenantId tenantId, String clazz);

}
