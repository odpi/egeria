/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securityofficer.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The SecurityOfficerOutTopicServerProvider provides a base class for the connector provider supporting
 * SecurityOfficerOutTopicServerConnector Connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * SecurityOfficerOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class SecurityOfficerOutTopicServerProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "734290fd-10eb-4a4b-9e36-93aae7fff539";
    static final String  connectorTypeName = "Security Officer Out Topic Server Connector";
    static final String  connectorTypeDescription = "Connector supports the sending of events on the Security Officer OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * governance service implementation.
     */
    public SecurityOfficerOutTopicServerProvider()
    {
        Class<?> connectorClass = SecurityOfficerOutTopicServerConnector.class;

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