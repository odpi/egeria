/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraph;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraph;

import java.util.Set;

public interface BufferGraph extends OpenLineageGraph {

    /**
     * Stores
     *
     * @param lineageEntity event
     */
    void addEntity(LineageEntity lineageEntity);

    /**
     * Process the serialized  information view event
     *
     * @param lineageEvent event
     */
    void updateEntity(LineageEvent lineageEvent);

    /**
     * Task that the scheduler performs based on the interval
     *
     */
    void schedulerTask();

    void setMainGraph(Object mainGraph);

}
