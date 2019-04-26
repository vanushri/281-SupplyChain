
import java.util.List;
import java.util.Optional;


public class BaseComponentDescriptorService implements ComponentDescriptorService {

    @Autowired
    private ComponentDescriptorDao componentDescriptorDao;

    @Override
    public ComponentDescriptor saveComponent(TenantId tenantId, ComponentDescriptor component) {
        componentValidator.validate(component, data -> new TenantId(EntityId.NULL_UUID));
        Optional<ComponentDescriptor> result = componentDescriptorDao.saveIfNotExist(tenantId, component);
        if (result.isPresent()) {
            return result.get();
        } else {
            return componentDescriptorDao.findByClazz(tenantId, component.getClazz());
        }
    }

    @Override
    public ComponentDescriptor findById(TenantId tenantId, ComponentDescriptorId componentId) {
        Validator.validateId(componentId, "Incorrect component id for search request.");
        return componentDescriptorDao.findById(tenantId, componentId);
    }

    @Override
    public ComponentDescriptor findByClazz(TenantId tenantId, String clazz) {
        Validator.validateString(clazz, "Incorrect clazz for search request.");
        return componentDescriptorDao.findByClazz(tenantId, clazz);
    }

    @Override
    public TextPageData<ComponentDescriptor> findByTypeAndPageLink(TenantId tenantId, ComponentType type, TextPageLink pageLink) {
        Validator.validatePageLink(pageLink, "Incorrect PageLink object for search plugin components request.");
        List<ComponentDescriptor> components = componentDescriptorDao.findByTypeAndPageLink(tenantId, type, pageLink);
        return new TextPageData<>(components, pageLink);
    }

    @Override
    public TextPageData<ComponentDescriptor> findByScopeAndTypeAndPageLink(TenantId tenantId, ComponentScope scope, ComponentType type, TextPageLink pageLink) {
        Validator.validatePageLink(pageLink, "Incorrect PageLink object for search plugin components request.");
        List<ComponentDescriptor> components = componentDescriptorDao.findByScopeAndTypeAndPageLink(tenantId, scope, type, pageLink);
        return new TextPageData<>(components, pageLink);
    }

    @Override
    public void deleteByClazz(TenantId tenantId, String clazz) {
        Validator.validateString(clazz, "Incorrect clazz for delete request.");
        componentDescriptorDao.deleteByClazz(tenantId, clazz);
    }

    @Override
    public boolean validate(TenantId tenantId, ComponentDescriptor component, JsonNode configuration) {
        JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();
        try {
            if (!component.getConfigurationDescriptor().has("schema")) {
                throw new DataValidationException("Configuration descriptor doesn't contain schema property!");
            }
            JsonNode configurationSchema = component.getConfigurationDescriptor().get("schema");
            ProcessingReport report = validator.validate(configurationSchema, configuration);
            return report.isSuccess();
        } catch (ProcessingException e) {
            throw new IncorrectParameterException(e.getMessage(), e);
        }
    }

    private DataValidator<ComponentDescriptor> componentValidator =
            new DataValidator<ComponentDescriptor>() {
                @Override
                protected void validateDataImpl(TenantId tenantId, ComponentDescriptor plugin) {
                    if (plugin.getType() == null) {
                        throw new DataValidationException("Component type should be specified!.");
                    }
                    if (plugin.getScope() == null) {
                        throw new DataValidationException("Component scope should be specified!.");
                    }
                    if (StringUtils.isEmpty(plugin.getName())) {
                        throw new DataValidationException("Component name should be specified!.");
                    }
                    if (StringUtils.isEmpty(plugin.getClazz())) {
                        throw new DataValidationException("Component clazz should be specified!.");
                    }
                }
            };
}
