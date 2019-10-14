/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageConnectorBase;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.JanusFactory.openBufferGraph;

public class JanusConnector extends OpenLineageConnectorBase {

    private static final Logger log = LoggerFactory.getLogger(JanusConnector.class);
    private JanusGraph graph;

    /**
     * Initialize the connector.
     *
     * @param connectorInstanceId  - unique id for the connector instance - useful for messages etc
     * @param connectionProperties - POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties) {

        super.initialize(connectorInstanceId, connectionProperties);
        this.connectionProperties = connectionProperties;
        this.graph = openBufferGraph(connectionProperties);

    }

    @Override
    public void addEntity(ProcessLineageEvent processLineageEvent) {
    }
}
