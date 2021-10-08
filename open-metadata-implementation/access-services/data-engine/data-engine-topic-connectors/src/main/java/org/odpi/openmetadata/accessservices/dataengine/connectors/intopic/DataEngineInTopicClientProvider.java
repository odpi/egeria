/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.connectors.intopic;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * The DataEngineInTopicClientProvider provides a base class for the connector provider supporting
 * DataEngine topic connectors.
 *
 * It extends ConnectorProviderBase which does the creation of connector instances.  The subclasses of
 * DataEngineInTopicClientProvider must initialize ConnectorProviderBase with the Java class
 * name of their Connector implementation (by calling super.setConnectorClassName(className)).
 * Then the connector provider will work.
 */
public class DataEngineInTopicClientProvider extends ConnectorProviderBase
{
    static final String CONNECTOR_TYPE_GUID = "8d9932c4-3cf6-4e5e-aee9-30f14a841981";
    static final String CONNECTOR_TYPE_NAME = "Data Engine In Topic Client Connector";
    static final String CONNECTOR_TYPE_DESCRIPTION = "Connector supports sending events on the Data Engine input topic.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * discovery service implementation.
     */
    public DataEngineInTopicClientProvider()
    {
        Class<?> connectorClass = DataEngineInTopicClientConnector.class;

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