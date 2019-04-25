package dao.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dao.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class CassandraRuleChainDao extends CassandraAbstractSearchTextDao<RuleChainEntity, RuleChain> implements RuleChainDao {

    @Override
    protected Class<RuleChainEntity> getColumnFamilyClass() {
        return RuleChainEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return RULE_CHAIN_COLUMN_FAMILY_NAME;
    }

    @Override
    public List<RuleChain> findRuleChainsByTenantId(UUID tenantId, TextPageLink pageLink) {
        log.debug("Try to find rule chains by tenantId [{}] and pageLink [{}]", tenantId, pageLink);
        List<RuleChainEntity> ruleChainEntities = findPageWithTextSearch(new TenantId(tenantId), RULE_CHAIN_BY_TENANT_AND_SEARCH_TEXT_COLUMN_FAMILY_NAME,
                Collections.singletonList(eq(RULE_CHAIN_TENANT_ID_PROPERTY, tenantId)),
                pageLink);

        log.trace("Found rule chains [{}] by tenantId [{}] and pageLink [{}]", ruleChainEntities, tenantId, pageLink);
        return DaoUtil.convertDataList(ruleChainEntities);
    }

}
