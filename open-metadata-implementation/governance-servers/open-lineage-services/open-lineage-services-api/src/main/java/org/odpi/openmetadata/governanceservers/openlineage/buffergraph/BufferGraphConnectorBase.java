/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.buffergraph;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.ffdc.OpenLineageException;

public abstract class BufferGraphConnectorBase extends ConnectorBase implements BufferGraph {

    @Override
    public abstract void initializeGraphDB() throws OpenLineageException;

    @Override
    public abstract void addEntity(LineageEvent lineageEvent);

    @Override
    public void updateEntity(LineageEvent lineageEvent) {

    }

    @Override
    public void deleteEntity(String guid) {

    }

    @Override
    public abstract void schedulerTask();

    @Override
    public abstract void setMainGraph(Object mainGraph);

}
