/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraph;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraph;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;

import java.util.Set;

public interface BufferGraph extends OpenLineageGraph {

    /**
     * Stores
     *
     * @param lineageEvent event
     */
    void addEntity(LineageEvent lineageEvent);

    void initializeGraphDB() throws OpenLineageException;

    /**
     * Updates a vertex in the Graph
     *
     * @param lineageEvent event
     */
    void updateEntity(LineageEvent lineageEvent);

    /**
     * Deletes a vertex in the graph
     *
     * @param guid unique identifier of the entity to be deleted
     */
    void deleteEntity(String guid);

    /**
     * Task that the scheduler performs based on the interval
     *
     */
    void schedulerTask();

    void setMainGraph(Object mainGraph);

}
