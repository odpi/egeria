/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceserver.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The GovernanceEngineOutTopicClientProvider provides a base class for the connector provider supporting
 * GovernanceEngineOutTopicClientConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * GovernanceEngineOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class GovernanceServerOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "76395ced-d0a8-493b-bc22-83b65d98b977";
    static final String  connectorTypeName = "Governance Server Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events on the Governance Server OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * governance service implementation.
     */
    public GovernanceServerOutTopicClientProvider()
    {
        Class<?> connectorClass = GovernanceServerOutTopicClientConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}