/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

public class OpenLineageConnectorBase extends ConnectorBase implements OpenLineageGraphStore {

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {
        super.initialize(connectorInstanceId,connectionProperties);
    }

    /**
     * Process the serialized  information view event
     *
     * @param processLineageEvent event
     * @return the table of created views for generating the kafka events
     */
    @Override
    public void addEntity(ProcessLineageEvent processLineageEvent) { }
}
