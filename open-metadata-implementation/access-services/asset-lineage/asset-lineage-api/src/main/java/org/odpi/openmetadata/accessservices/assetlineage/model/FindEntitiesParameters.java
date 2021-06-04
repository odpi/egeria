/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class FindEntitiesParameters {
    private Long updatedAfter;
    private List<String> entitySubtypeGUIDs;
    private List<InstanceStatus> limitResultsByStatus;
    private SearchClassifications searchClassifications;
    private String sequencingProperty;
    private SequencingOrder sequencingOrder;

    private FindEntitiesParameters(Long updatedAfter, List<String> entitySubtypeGUIDs, List<InstanceStatus> limitResultsByStatus, SearchClassifications searchClassifications, String sequencingProperty, SequencingOrder sequencingOrder) {
        this.updatedAfter = updatedAfter;
        this.entitySubtypeGUIDs = entitySubtypeGUIDs;
        this.limitResultsByStatus = limitResultsByStatus;
        this.searchClassifications = searchClassifications;
        this.sequencingProperty = sequencingProperty;
        this.sequencingOrder = sequencingOrder;
    }

    public Long getUpdatedAfter() {
        return updatedAfter;
    }

    public List<String> getEntitySubtypeGUIDs() {
        return entitySubtypeGUIDs;
    }

    public List<InstanceStatus> getLimitResultsByStatus() {
        return limitResultsByStatus;
    }

    public SearchClassifications getSearchClassifications() {
        return searchClassifications;
    }

    public String getSequencingProperty() {
        return sequencingProperty;
    }

    public SequencingOrder getSequencingOrder() {
        return sequencingOrder;
    }

    static public class Builder {
        private Long updatedAfter;
        private List<String> entitySubtypeGUIDs;
        private List<InstanceStatus> limitResultsByStatus;
        private SearchClassifications searchClassifications;
        private String sequencingProperty;
        private SequencingOrder sequencingOrder;

        public Builder() {
        }

        public Builder withEntitySubtypeGUIDs(List<String> entitySubtypeGUIDs) {
            this.entitySubtypeGUIDs = entitySubtypeGUIDs;
            return this;
        }

        public Builder withLimitResultsByStatus(List<InstanceStatus> limitResultsByStatus) {
            this.limitResultsByStatus = limitResultsByStatus;
            return this;
        }

        public Builder withSearchClassifications(SearchClassifications searchClassifications) {
            this.searchClassifications = searchClassifications;
            return this;
        }

        public Builder withSequencingProperty(String sequencingProperty) {
            this.sequencingProperty = sequencingProperty;
            return this;
        }

        public Builder withSequencingOrder(SequencingOrder sequencingOrder) {
            this.sequencingOrder = sequencingOrder;
            return this;
        }

        public Builder withUpdatedAfter(LocalDateTime updatedAfterDate) {
            if (updatedAfterDate != null) {
                this.updatedAfter = updatedAfterDate.atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
            } else {
                this.updatedAfter = 0L;
            }
            return this;
        }

        public FindEntitiesParameters build() {
            return new FindEntitiesParameters(updatedAfter, entitySubtypeGUIDs, limitResultsByStatus, searchClassifications,
                    sequencingProperty, sequencingOrder);
        }
    }

}
