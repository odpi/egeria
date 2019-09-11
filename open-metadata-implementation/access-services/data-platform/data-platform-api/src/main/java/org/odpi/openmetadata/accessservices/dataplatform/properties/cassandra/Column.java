/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties.cassandra;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.dataplatform.properties.BusinessTerm;
import org.odpi.openmetadata.accessservices.dataplatform.properties.Source;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The type Column.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Column extends Source {

    private String name;
    private String type;
    private BusinessTerm businessTerms;
    private boolean isPartitionKey;
    private boolean isPrimaryKey;
    private boolean isClusteringKey;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets business terms.
     *
     * @return the business terms
     */
    public BusinessTerm getBusinessTerms() {
        return businessTerms;
    }

    /**
     * Sets business terms.
     *
     * @param businessTerms the business terms
     */
    public void setBusinessTerms(BusinessTerm businessTerms) {
        this.businessTerms = businessTerms;
    }

    /**
     * Is partition key boolean.
     *
     * @return the boolean
     */
    public boolean isPartitionKey() {
        return isPartitionKey;
    }

    /**
     * Sets partition key.
     *
     * @param partitionKey the partition key
     */
    public void setPartitionKey(boolean partitionKey) {
        isPartitionKey = partitionKey;
    }

    /**
     * Is primary key boolean.
     *
     * @return the boolean
     */
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    /**
     * Sets primary key.
     *
     * @param primaryKey the primary key
     */
    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    /**
     * Is clustering key boolean.
     *
     * @return the boolean
     */
    public boolean isClusteringKey() {
        return isClusteringKey;
    }

    /**
     * Sets clustering key.
     *
     * @param clusteringKey the clustering key
     */
    public void setClusteringKey(boolean clusteringKey) {
        isClusteringKey = clusteringKey;
    }

    @Override
    public String toString() {
        return "Column{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", businessTerms=" + businessTerms +
                ", isPartitionKey=" + isPartitionKey +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isClusteringKey=" + isClusteringKey +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", guid='" + guid + '\'' +
                "} " + super.toString();
    }
}
