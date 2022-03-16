/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The ITInfrastructureOutTopicServerProvider provides a base class for the connector provider supporting
 * ITInfrastructureOutTopicServerConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * ITInfrastructureOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class ITInfrastructureOutTopicServerProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "734290fd-10eb-4a4b-9e36-93aae7fff539";
    static final String  connectorTypeName = "IT Infrastructure Out Topic Server Connector";
    static final String  connectorTypeDescription = "Connector supports the sending of events on the IT Infrastructure OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public ITInfrastructureOutTopicServerProvider()
    {
        Class<?> connectorClass = ITInfrastructureOutTopicServerConnector.class;

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