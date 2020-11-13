/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.restclients.spring;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * SpringRESTClientConnectorProvider provides the connector provider for the SpringRESTClientConnector.
 */
public class SpringRESTClientConnectorProvider extends RESTClientConnectorProvider
{
    static final String  connectorTypeGUID = "434cd526-ecea-4e14-b8f5-97c2c1d44fa";
    static final String  connectorTypeName = "Spring REST Client Connector";
    static final String  connectorTypeDescription = "Connector that calls the REST API of a remote server using Spring.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * REST Client Connector implementation.
     */
    public SpringRESTClientConnectorProvider()
    {
        Class<?>    connectorClass = SpringRESTClientConnector.class;

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
