/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraph;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
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
     */
    void schedulerTask();

    void setMainGraph(Object mainGraph);

}
