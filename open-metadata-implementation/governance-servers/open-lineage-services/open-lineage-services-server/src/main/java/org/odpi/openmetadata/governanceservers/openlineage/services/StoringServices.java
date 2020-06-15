/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.governanceservers.openlineage.graph.LineageGraph;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StoringServices {

    private static final Logger log = LoggerFactory.getLogger(StoringServices.class);

    private LineageGraph lineageGraph;
    private JobConfiguration jobConfiguration;

    public StoringServices(LineageGraph graphStore) {
        this.lineageGraph = graphStore;
        this.jobConfiguration = new JobConfiguration(graphStore);
    }

    /**
     * Delegates the call for the creation of entities and relationships to the connector
     */
    public void addEntity(LineageEvent lineageEvent) {

        Set<GraphContext> verticesToBeAdded = new HashSet<>();
        lineageEvent.getAssetContext().forEach((key, value) -> {
            if (value.size() > 1) {
                verticesToBeAdded.addAll(value);
            } else {
                verticesToBeAdded.add(value.stream().findFirst().get());
            }
        });

        lineageGraph.storeToGraph(verticesToBeAdded);
    }

    /**
     * Delegates the call for the update of an entity to the connector
     *
     */
    public void updateEntity(LineageEvent lineageEvent) {
        log.debug("Open Lineage Services is processing a UpdateEntity event which contains the following entity with guid : {}", lineageEvent.getLineageEntity().getGuid());
        lineageGraph.updateEntity(lineageEvent.getLineageEntity());
    }

    /**
     * Delegates the call for the update of a relationship to the connector
     *
     */
    public void updateRelationship(LineageRelationshipEvent lineageRelationshipEvent) {
        log.debug("Open Lineage Services is processing a UpdateRelationshipEvent event which contains the following relantionhsip with guid: {}", lineageRelationshipEvent.getLineageRelationship().getGuid());
        lineageGraph.updateRelationship(lineageRelationshipEvent.getLineageRelationship());
    }

    /**
     * Delegates the call for the update of a classification to the connector
     */
    public void updateClassification(LineageEvent lineageEvent){
        log.debug("Open Lineage Services is processing an UpdateClassificationEvent event");
        lineageGraph.updateClassification(lineageEvent.getAssetContext());
    }

    /**
     * Delegates the call for the deletion of an entity to the connector
     */
    public void deleteEntity(LineageEvent lineageEvent) {

        lineageGraph.deleteEntity(lineageEvent.getLineageEntity().getGuid(), lineageEvent.getLineageEntity().getVersion());
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
    public void deleteClassification(LineageEvent lineageEvent){
        log.debug("Open Lineage Services is processing an UpdateClassificationEvent event");
        Map<String, Set<GraphContext>> empty = new HashMap<>();

        lineageGraph.deleteClassification(lineageEvent.getLineageEntity().getGuid(),
                lineageEvent.getAssetContext() == null ? empty : lineageEvent.getAssetContext() );
    }
}
