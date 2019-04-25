package dao.cassandra;

import javax.annotation.PostConstruct;

public class CassandraCluster extends CassandraCluster {

    @Value("${cassandra.keyspace_name}")
    private String keyspaceName;

    @PostConstruct
    public void init() {
        super.init(keyspaceName);
    }

}
