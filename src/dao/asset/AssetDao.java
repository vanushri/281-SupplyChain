package dao.asset;

import dao.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The Interface AssetDao.
 *
 */
public interface AssetDao extends Dao<Asset> {

    /**
     * Save or update asset object
     *
     * @param asset the asset object
     * @return saved asset object
     */
    Asset save(TenantId tenantId, Asset asset);

    /**
     * Find assets by tenantId and page link.
     *
     * @param tenantId the tenantId
     * @param pageLink the page link
     * @return the list of asset objects
     */
    List<Asset> findAssetsByTenantId(UUID tenantId, TextPageLink pageLink);

    /**
     * Find assets by tenantId, type and page link.
     *
     * @param tenantId the tenantId
     * @param type the type
     * @param pageLink the page link
     * @return the list of asset objects
     */
    List<Asset> findAssetsByTenantIdAndType(UUID tenantId, String type, TextPageLink pageLink);

    /**
     * Find assets by tenantId and assets Ids.
     *
     * @param tenantId the tenantId
     * @param assetIds the asset Ids
     * @return the list of asset objects
     */
    ListenableFuture<List<Asset>> findAssetsByTenantIdAndIdsAsync(UUID tenantId, List<UUID> assetIds);

    /**
     * Find assets by tenantId, customerId and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param pageLink the page link
     * @return the list of asset objects
     */
    List<Asset> findAssetsByTenantIdAndCustomerId(UUID tenantId, UUID customerId, TextPageLink pageLink);

    /**
     * Find assets by tenantId, customerId, type and page link.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param type the type
     * @param pageLink the page link
     * @return the list of asset objects
     */
    List<Asset> findAssetsByTenantIdAndCustomerIdAndType(UUID tenantId, UUID customerId, String type, TextPageLink pageLink);

    /**
     * Find assets by tenantId, customerId and assets Ids.
     *
     * @param tenantId the tenantId
     * @param customerId the customerId
     * @param assetIds the asset Ids
     * @return the list of asset objects
     */
    ListenableFuture<List<Asset>> findAssetsByTenantIdAndCustomerIdAndIdsAsync(UUID tenantId, UUID customerId, List<UUID> assetIds);

    /**
     * Find assets by tenantId and asset name.
     *
     * @param tenantId the tenantId
     * @param name the asset name
     * @return the optional asset object
     */
    Optional<Asset> findAssetsByTenantIdAndName(UUID tenantId, String name);

    /**
     * Find tenants asset types.
     *
     * @return the list of tenant asset type objects
     */
    ListenableFuture<List<EntitySubtype>> findTenantAssetTypesAsync(UUID tenantId);

}
