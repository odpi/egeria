/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraphstore;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphStore;

public interface BufferGraph extends OpenLineageGraphStore {

    /**
     * Process the serialized  information view event
     *
     * @param lineageEvent event
     */
    void addEntity(LineageEvent lineageEvent);

    /**
     * Task that the scheduler performs based on the interval
     *
     */
    void schedulerTask();

    void setMainGraph(Object mainGraph);

}
