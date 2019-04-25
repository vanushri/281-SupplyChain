package dao.rule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import dao.*;

public class CassandraRuleNodeDao extends CassandraAbstractSearchTextDao<RuleNodeEntity, RuleNode> implements RuleNodeDao {

    @Override
    protected Class<RuleNodeEntity> getColumnFamilyClass() {
        return RuleNodeEntity.class;
    }

    @Override
    protected String getColumnFamilyName() {
        return RULE_NODE_COLUMN_FAMILY_NAME;
    }

}
