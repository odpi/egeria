/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEntityEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipsEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageSyncEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class StoringServices {

    private static final Logger log = LoggerFactory.getLogger(StoringServices.class);

    private final LineageGraph lineageGraph;

    public StoringServices(LineageGraph graphStore) {
        this.lineageGraph = graphStore;
    }

    /**
     * Delegates the call for the creation of entities and relationships to the connector
     */
    public void addEntityContext(LineageRelationshipsEvent lineageRelationshipsEvent) {
        lineageGraph.storeToGraph(lineageRelationshipsEvent.getRelationshipsContext().getRelationships());
    }

    /**
     * Delegates the call for the creation of entities and relationships to the connector
     */
    public void upsertEntityContext(LineageRelationshipsEvent lineageRelationshipsEvent) {
        String termGUID = lineageRelationshipsEvent.getRelationshipsContext().getEntityGuid();
        lineageGraph.storeToGraph(lineageRelationshipsEvent.getRelationshipsContext().getRelationships());
    }
    /**
     * Delegates the call for the creation of entities and relationships to the connector
     */
    public void addEntityContext(Set<GraphContext> relationships) {
        lineageGraph.storeToGraph(relationships);
    }

    /**
     * Delegates the call for the update of an entity to the connector
     */
    public void updateEntity(LineageEntityEvent lineageEntityEvent) {
        log.debug("Open Lineage Services is processing a UpdateEntity event which contains the following entity with guid : {}",
                lineageEntityEvent.getLineageEntity().getGuid());
        lineageGraph.updateEntity(lineageEntityEvent.getLineageEntity());
    }

    /**
     * Delegates the call for the update of a relationship to the connector
     */
    public void updateRelationship(LineageRelationshipEvent lineageRelationshipEvent) {
        log.debug("Open Lineage Services is processing a UpdateRelationshipEvent event which contains the following relationship with guid: {}",
                lineageRelationshipEvent.getLineageRelationship().getGuid());
        lineageGraph.updateRelationship(lineageRelationshipEvent.getLineageRelationship());
    }

    /**
     * Delegates the call for the update of a classification to the connector
     */
    public void updateClassification(LineageRelationshipsEvent lineageRelationshipsEvent) {
        lineageGraph.updateClassification(lineageRelationshipsEvent.getRelationshipsContext().getRelationships());
    }

    /**
     * Delegates the call for the deletion of an entity to the connector
     */
    public void deleteEntity(LineageEntityEvent lineageEntityEvent) {

        lineageGraph.deleteEntity(lineageEntityEvent.getLineageEntity().getGuid(), lineageEntityEvent.getLineageEntity().getVersion());
    }

    public void deleteRelationship(LineageRelationshipEvent lineageRelationshipEvent) {
        lineageGraph.deleteRelationship(lineageRelationshipEvent.getLineageRelationship().getGuid());
    }

    /**
     * Delegates the call for creating or adding a relationship to the connector
     *
     * @param lineageRelationshipEvent the transformed event based on which the relationship will be created
     */
    public void upsertRelationship(LineageRelationshipEvent lineageRelationshipEvent) {
        lineageGraph.upsertRelationship(lineageRelationshipEvent.getLineageRelationship());
    }

    /**
     * Delegates the call to delete a classification to the connector
     */
    public void deleteClassification(LineageRelationshipsEvent lineageRelationshipsEvent) {
        log.debug("Open Lineage Services is processing an DeleteClassificationEvent event");

        lineageGraph.deleteClassification(lineageRelationshipsEvent.getRelationshipsContext().getRelationships());
    }

    /**
     * Applies lineageSyncEvent using lineage storage connector.
     * @param lineageSyncEvent event to be delegated to the respective connector interface.
     */
    public void apply(LineageSyncEvent lineageSyncEvent) {
        // publishSummary
        if (lineageSyncEvent.getPublishSummary() != null) {
            lineageGraph.saveAssetLineageUpdateTime(lineageSyncEvent.getPublishSummary().getLineageTimestamp());
        }
        // updateSummary...
        if (lineageSyncEvent.getSyncUpdateContext() != null) {
            lineageGraph.updateNeighbours(lineageSyncEvent.getSyncUpdateContext().getEntityGUID(),
                    lineageSyncEvent.getSyncUpdateContext().getNeighboursGUID());
        }
    }

    /**
     * Check entity existence boolean in the graph through the lineage connector.
     *
     * @param guid the guid
     * @return the boolean
     */
    public boolean isEntityInGraph(String guid) {
        return lineageGraph.isEntityInGraph(guid);
    }
}
