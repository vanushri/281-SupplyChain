package dao.entityview;

import dao.*;

public class CassandraEntityViewDao extends CassandraAbstractSearchTextDao<EntityViewEntity, EntityView> implements EntityViewDao {

    @Override
    protected Class<EntityViewEntity> getColumnFamilyClass() {
        return EntityViewEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return ENTITY_VIEW_TABLE_FAMILY_NAME;
    }

    @Override
    public EntityView save(TenantId tenantId, EntityView domain) {
        EntityView savedEntityView = super.save(domain.getTenantId(), domain);
        EntitySubtype entitySubtype = new EntitySubtype(savedEntityView.getTenantId(), EntityType.ENTITY_VIEW, savedEntityView.getType());
        EntitySubtypeEntity entitySubtypeEntity = new EntitySubtypeEntity(entitySubtype);
        Statement saveStatement = cluster.getMapper(EntitySubtypeEntity.class).saveQuery(entitySubtypeEntity);
        executeWrite(tenantId, saveStatement);
        return savedEntityView;
    }

    @Override
    public List<EntityView> findEntityViewsByTenantId(UUID tenantId, TextPageLink pageLink) {
        log.debug("Try to find entity views by tenantId [{}] and pageLink [{}]", tenantId, pageLink);
        List<EntityViewEntity> entityViewEntities =
                findPageWithTextSearch(new TenantId(tenantId), ENTITY_VIEW_BY_TENANT_AND_SEARCH_TEXT_CF,
                Collections.singletonList(eq(TENANT_ID_PROPERTY, tenantId)), pageLink);
        log.trace("Found entity views [{}] by tenantId [{}] and pageLink [{}]",
                entityViewEntities, tenantId, pageLink);
        return DaoUtil.convertDataList(entityViewEntities);
    }

    @Override
    public List<EntityView> findEntityViewsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink) {
        log.debug("Try to find entity views by tenantId [{}], type [{}] and pageLink [{}]", tenantId, type, pageLink);
        List<EntityViewEntity> entityViewEntities =
                findPageWithTextSearch(new TenantId(tenantId), ENTITY_VIEW_BY_TENANT_BY_TYPE_AND_SEARCH_TEXT_CF,
                        Arrays.asList(eq(ENTITY_VIEW_TYPE_PROPERTY, type),
                                eq(TENANT_ID_PROPERTY, tenantId)), pageLink);
        log.trace("Found entity views [{}] by tenantId [{}], type [{}] and pageLink [{}]",
                entityViewEntities, tenantId, type, pageLink);
        return DaoUtil.convertDataList(entityViewEntities);
    }

    @Override
    public Optional<EntityView> findEntityViewByTenantIdAndName(UUID tenantId, String name) {
        Select.Where query = select().from(ENTITY_VIEW_BY_TENANT_AND_NAME).where();
        query.and(eq(ENTITY_VIEW_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(ENTITY_VIEW_NAME_PROPERTY, name));
        return Optional.ofNullable(DaoUtil.getData(findOneByStatement(new TenantId(tenantId), query)));
    }

    @Override
    public List<EntityView> findEntityViewsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink) {
        log.debug("Try to find entity views by tenantId [{}], customerId[{}] and pageLink [{}]",
                tenantId, customerId, pageLink);
        List<EntityViewEntity> entityViewEntities = findPageWithTextSearch(new TenantId(tenantId),
                ENTITY_VIEW_BY_TENANT_AND_CUSTOMER_CF,
                Arrays.asList(eq(CUSTOMER_ID_PROPERTY, customerId), eq(TENANT_ID_PROPERTY, tenantId)),
                pageLink);
        log.trace("Found find entity views [{}] by tenantId [{}], customerId [{}] and pageLink [{}]",
                entityViewEntities, tenantId, customerId, pageLink);
        return DaoUtil.convertDataList(entityViewEntities);
    }

    @Override
    public List<EntityView> findEntityViewsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink) {
        log.debug("Try to find entity views by tenantId [{}], customerId[{}], type [{}] and pageLink [{}]",
                tenantId, customerId, type, pageLink);
        List<EntityViewEntity> entityViewEntities = findPageWithTextSearch(new TenantId(tenantId),
                ENTITY_VIEW_BY_TENANT_AND_CUSTOMER_AND_TYPE_CF,
                Arrays.asList(eq(DEVICE_TYPE_PROPERTY, type), eq(CUSTOMER_ID_PROPERTY, customerId), eq(TENANT_ID_PROPERTY, tenantId)),
                pageLink);
        log.trace("Found find entity views [{}] by tenantId [{}], customerId [{}], type [{}] and pageLink [{}]",
                entityViewEntities, tenantId, customerId, type, pageLink);
        return DaoUtil.convertDataList(entityViewEntities);
    }

    @Override
    public ListenableFuture<List<EntityView>> findEntityViewsByTenantIdAndEntityIdAsync(UUID tenantId, UUID entityId) {
        log.debug("Try to find entity views by tenantId [{}] and entityId [{}]", tenantId, entityId);
        Select.Where query = select().from(ENTITY_VIEW_BY_TENANT_AND_ENTITY_ID_CF).where();
        query.and(eq(TENANT_ID_PROPERTY, tenantId));
        query.and(eq(ENTITY_ID_COLUMN, entityId));
        return findListByStatementAsync(new TenantId(tenantId), query);
    }

    @Override
    public ListenableFuture<List<EntitySubtype>> findTenantEntityViewTypesAsync(UUID tenantId) {
        Select select = select().from(ENTITY_SUBTYPE_COLUMN_FAMILY_NAME);
        Select.Where query = select.where();
        query.and(eq(ENTITY_SUBTYPE_TENANT_ID_PROPERTY, tenantId));
        query.and(eq(ENTITY_SUBTYPE_ENTITY_TYPE_PROPERTY, EntityType.ENTITY_VIEW));
        query.setConsistencyLevel(cluster.getDefaultReadConsistencyLevel());
        ResultSetFuture resultSetFuture = executeAsyncRead(new TenantId(tenantId), query);
        return Futures.transform(resultSetFuture, new Function<ResultSet, List<EntitySubtype>>() {
            @Nullable
            @Override
            public List<EntitySubtype> apply(@Nullable ResultSet resultSet) {
                Result<EntitySubtypeEntity> result = cluster.getMapper(EntitySubtypeEntity.class).map(resultSet);
                if (result != null) {
                    List<EntitySubtype> entitySubtypes = new ArrayList<>();
                    result.all().forEach((entitySubtypeEntity) ->
                            entitySubtypes.add(entitySubtypeEntity.toEntitySubtype())
                    );
                    return entitySubtypes;
                } else {
                    return Collections.emptyList();
                }
            }
        });
    }
}
