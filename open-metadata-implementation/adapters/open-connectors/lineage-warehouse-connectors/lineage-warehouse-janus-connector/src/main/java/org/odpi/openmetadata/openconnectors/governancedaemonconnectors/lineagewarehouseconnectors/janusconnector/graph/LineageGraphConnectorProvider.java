/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.graph;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.governanceservers.lineagewarehouse.graph.LineageGraphProviderBase;

public class LineageGraphConnectorProvider extends LineageGraphProviderBase {

    static final String CONNECTOR_TYPE_GUID = "e2f657d6-e5bd-11e9-81b4-2a2ae2dbcce4";
    static final String CONNECTOR_TYPE_NAME = "Janus Graph Connector";
    static final String CONNECTOR_TYPE_DESCRIPTION = "Connector supports storing and retrieving entities for lineage from Janus Graph.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public LineageGraphConnectorProvider() {
        super();
        Class connectorClass = LineageGraphConnector.class;
        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(CONNECTOR_TYPE_GUID);
        connectorType.setQualifiedName(CONNECTOR_TYPE_NAME);
        connectorType.setDisplayName(CONNECTOR_TYPE_NAME);
        connectorType.setDescription(CONNECTOR_TYPE_DESCRIPTION);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }

}
