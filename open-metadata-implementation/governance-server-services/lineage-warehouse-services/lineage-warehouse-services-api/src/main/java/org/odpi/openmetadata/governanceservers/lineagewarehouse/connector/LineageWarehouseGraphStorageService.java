/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.connector;

import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageSyncUpdateContext;

import java.util.Optional;
import java.util.Set;

/**
 * LineageWarehouseGraphStorageService is the API of the Lineage Warehouse Connector's storage service.
 */
public interface LineageWarehouseGraphStorageService
{
    /**
     * Stores a lineage event into the lineage graph database
     *
     * @param graphContext graph Collection
     */
    void storeToGraph(Set<GraphContext> graphContext);


    /**
     * Updates the neighbours of a node by removing all the relationships that no longer have a direct link to the entity.
     *
     * @param  syncUpdateContext contains the context for syncing the relationships of a node after an update.
     */
    void updateNeighbours(LineageSyncUpdateContext syncUpdateContext);


    /**
     * Updates a vertex in the Graph
     *
     * @param lineageEntity entity to be updated
     */
    void updateEntity(LineageEntity lineageEntity);


    /**
     * Create or update the relationship between two edges.
     * If the vertexes do not yet exist, they are firstly created.
     *
     * @param lineageRelationship relationship to be updated or created
     * */
    void upsertRelationship(LineageRelationship lineageRelationship);


    /**
     * Updates a vertex in the Graph
     *
     * @param lineageRelationship relationship to be updated
     */
    void updateRelationship(LineageRelationship lineageRelationship);


    /**
     * Updates an entity's classification in the Graph
     *
     * @param classificationContext classification context
     */
    void updateClassification(Set<GraphContext> classificationContext);


    /**
     * Deletes an entity's classification in the Graph
     *
     * @param classificationContext classification context
     */
    void deleteClassification(Set<GraphContext> classificationContext);


    /**
     * Deletes a relationship in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     */
    void deleteRelationship(String guid);


    /**
     * Deletes a vertex in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     * @param version version of the entity to be deleted
     */
    void deleteEntity(String guid,Object version);


    /**
     * Save last asset lineage update time in the graph
     *
     * @param timestamp the standard epoch time in milliseconds
     */
    void saveAssetLineageUpdateTime(Long timestamp);


    /**
     * Gets last asset lineage update time from the graph
     *
     * @return last update time represented as epoch time milliseconds
     */
    Optional<Long> getAssetLineageUpdateTime();


    /**
     * Returns whether an entity exists in the graph or not
     *
     * @param guid the lineage entity guid
     * @return the boolean
     */
    Boolean isEntityInGraph(String guid);
}
