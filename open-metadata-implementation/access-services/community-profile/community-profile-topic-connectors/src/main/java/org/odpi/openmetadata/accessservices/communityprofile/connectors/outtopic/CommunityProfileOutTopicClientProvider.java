/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The CommunityProfileOutTopicClientProvider provides a base class for the connector provider supporting
 * CommunityProfileOutTopicClientConnector Connectors.
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * CommunityProfileOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class CommunityProfileOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "5ec48dfe-73ba-4cb1-852e-38da59337861";
    static final String  connectorTypeName = "Community Profile Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events on the Community Profile OMAS Out Topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * governance service implementation.
     */
    public CommunityProfileOutTopicClientProvider()
    {
        Class<?> connectorClass = CommunityProfileOutTopicClientConnector.class;

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