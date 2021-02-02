/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.model;

import java.util.Set;
/**
 * Contains a set of lineage relationship in the lineage graph with self contained properties.
 */
public class RelationshipsContext {
    private String entityGuid;
    private Set<GraphContext> relationships;

    public RelationshipsContext() {
    }

    public RelationshipsContext(String entityGuid, Set<GraphContext> relationships) {
        this.entityGuid = entityGuid;
        this.relationships = relationships;
    }

    public String getEntityGuid() {
        return entityGuid;
    }

    public void setEntityGuid(String entityGuid) {
        this.entityGuid = entityGuid;
    }

    public Set<GraphContext> getRelationships() {
        return relationships;
    }

    public void setRelationships(Set<GraphContext> relationships) {
        this.relationships = relationships;
    }

    @Override
    public String toString() {
        return "RelationshipsContext{" +
                "entityGuid='" + entityGuid + '\'' +
                ", relationships=" + relationships +
                '}';
    }
}
