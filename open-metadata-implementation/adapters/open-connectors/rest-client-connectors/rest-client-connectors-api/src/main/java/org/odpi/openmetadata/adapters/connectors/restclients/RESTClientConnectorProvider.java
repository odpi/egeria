/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * RESTClientConnectorProvider provides base class of the connector provider for the RESTClientConnector.
 */
public class RESTClientConnectorProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "6d432118-633e-4428-921c-271299cc6571";
    static final String  connectorTypeName = "REST Client Connector";
    static final String  connectorTypeDescription = "Connector that calls the REST API of a remote server.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * REST Client Connector implementation.  This constructor should be overridden in the connector
     * implementation.
     */
    public RESTClientConnectorProvider()
    {
        Class<?>    connectorClass = RESTClientConnector.class;

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
