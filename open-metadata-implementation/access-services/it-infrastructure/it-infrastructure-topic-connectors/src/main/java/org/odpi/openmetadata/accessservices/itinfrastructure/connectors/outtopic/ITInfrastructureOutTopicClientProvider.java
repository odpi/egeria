/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The ITInfrastructureOutTopicClientProvider provides a base class for the connector provider supporting
 * ITInfrastructureOutTopicClientConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * ITInfrastructureOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class ITInfrastructureOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "aa47afb1-e20b-47f0-abba-9eb1615ac33b";
    static final String  connectorTypeName = "IT Infrastructure Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events on the IT Infrastructure OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public ITInfrastructureOutTopicClientProvider()
    {
        Class<?> connectorClass = ITInfrastructureOutTopicClientConnector.class;

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