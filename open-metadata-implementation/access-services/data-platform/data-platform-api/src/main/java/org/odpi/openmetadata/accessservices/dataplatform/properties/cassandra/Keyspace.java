/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DatabaseSource;
import org.odpi.openmetadata.accessservices.dataplatform.properties.Source;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Keyspace is an asset in Apache Cassandra Database.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Keyspace extends Source {

    private DatabaseSource databaseSource;
    private String keyspaceName;
    private String replicationStrategy;
    private Map<String,String> replication;
    private List<Table> tableList;

    /**
     * Gets database source.
     *
     * @return the database source
     */
    public DatabaseSource getDatabaseSource() {
        return databaseSource;
    }

    /**
     * Sets database source.
     *
     * @param databaseSource the database source
     */
    public void setDatabaseSource(DatabaseSource databaseSource) {
        this.databaseSource = databaseSource;
    }

    /**
     * Gets keyspace name.
     *
     * @return the keyspace name
     */
    public String getKeyspaceName() {
        return keyspaceName;
    }

    /**
     * Sets keyspace name.
     *
     * @param keyspaceName the keyspace name
     */
    public void setKeyspaceName(String keyspaceName) {
        this.keyspaceName = keyspaceName;
    }

    /**
     * Gets replication strategy.
     *
     * @return the replication strategy
     */
    public String getReplicationStrategy() {
        return replicationStrategy;
    }

    /**
     * Sets replication strategy.
     *
     * @param replicationStrategy the replication strategy
     */
    public void setReplicationStrategy(String replicationStrategy) {
        this.replicationStrategy = replicationStrategy;
    }

    /**
     * Gets replication.
     *
     * @return the replication
     */
    public Map<String, String> getReplication() {
        return replication;
    }

    /**
     * Sets replication.
     *
     * @param replication the replication
     */
    public void setReplication(Map<String, String> replication) {
        this.replication = replication;
    }

    /**
     * Gets table list.
     *
     * @return the table list
     */
    public List<Table> getTableList() {
        return tableList;
    }

    /**
     * Sets table list.
     *
     * @param tableList the table list
     */
    public void setTableList(List<Table> tableList) {
        this.tableList = tableList;
    }

    @Override
    public String toString() {
        return "Keyspace{" +
                "databaseSource=" + databaseSource +
                ", keyspaceName='" + keyspaceName + '\'' +
                ", replicationStrategy='" + replicationStrategy + '\'' +
                ", replication=" + replication +
                ", tableList=" + tableList +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                "} " + super.toString();
    }
}