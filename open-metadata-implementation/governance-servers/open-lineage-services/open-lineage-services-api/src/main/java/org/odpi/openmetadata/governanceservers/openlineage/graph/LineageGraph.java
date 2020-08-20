/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.graph;

import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphConnector;

import java.util.Map;
import java.util.Set;

public interface LineageGraph extends OpenLineageGraphConnector {

    /**
     * Stores a lineage event into the lineage graph database
     *
     * @param graphContext graph Collection
     */
    void storeToGraph(Set<GraphContext> graphContext);

    /**
     * Updates a vertex in the Graph
     *
     * @param lineageEntity entity to be updated
     */
    void updateEntity(LineageEntity lineageEntity);

    /**
     * Create or update the relationship between two edges
     * In case the vertexes are not created, they are firstly created
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
     * @param assetContext asset context
     */
    void updateClassification(Map<String, Set<GraphContext>> assetContext);

    /**
     * Deletes an entity's classification in the Graph
     *
     * @param entityGuid entity guid
     * @param classificationContext classification context
     */
    void deleteClassification(String entityGuid, Map<String, Set<GraphContext>> classificationContext);

    /**
     * Deletes a relationship in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     */
    void deleteRelationship(String guid);

    /**
    /**
     * Deletes a vertex in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     * @param version version of the entity to be deleted
     */
    void deleteEntity(String guid,Object version);

    /**
     * Task that the scheduler performs based on the interval
     */
    void schedulerTask();

}
