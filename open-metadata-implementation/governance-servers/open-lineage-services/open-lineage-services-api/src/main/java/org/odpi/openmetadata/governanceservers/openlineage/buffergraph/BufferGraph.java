/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraph;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphConnector;

public interface BufferGraph extends OpenLineageGraphConnector {

    /**
     * Stores a lineage event into the Buffergraph database
     *
     * @param lineageEvent event
     */
    void addEntity(LineageEvent lineageEvent);

    /**
     * Updates a vertex in the Graph
     *
     * @param lineageEntity entity to be updated
     */
    void updateEntity(LineageEntity lineageEntity);

    /**
     * Updates a vertex in the Graph
     *
     * @param lineageRelationship relationship to be updated
     */
    void updateRelationship(LineageRelationship lineageRelationship);

    /**
     * Deletes a vertex in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     * @param version version of the entity to be deleted
     */
    void deleteEntity(String guid,String version);

    /**
     * Task that the scheduler performs based on the interval
     */
    void schedulerTask();

    void setMainGraph(Object mainGraph);

}
