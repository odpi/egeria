/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.connectors.outtopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The OMFOutTopicClientProvider provides a base class for the connector provider supporting
 * OMFOutTopicClientConnector Connectors.
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * OMFOutTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class OMFOutTopicClientProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "9a9062a2-0245-4d1d-b4d0-a0d84aa18387";
    static final String  connectorTypeName = "OMF Out Topic Client Connector";
    static final String  connectorTypeDescription = "Connector supports the receipt of events from the OMF Services.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public OMFOutTopicClientProvider()
    {
        Class<?> connectorClass = OMFOutTopicClientConnector.class;

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}