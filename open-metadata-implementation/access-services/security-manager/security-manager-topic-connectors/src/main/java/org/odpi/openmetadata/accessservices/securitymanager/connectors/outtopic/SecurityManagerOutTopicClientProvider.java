/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The SecurityManagerOutTopicClientProvider provides a base class for the connector provider supporting
 * SecurityManagerOutTopicClientConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * SecurityManagerOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class SecurityManagerOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "91bda022-184f-4c1c-b62a-c311f0e724f4";
    static final String  connectorTypeName = "Security Manager Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events on the Security Manager OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public SecurityManagerOutTopicClientProvider()
    {
        Class<?> connectorClass = SecurityManagerOutTopicClientConnector.class;

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