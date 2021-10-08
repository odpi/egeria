/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.elasticsearch;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * OpenAPIMonitorIntegrationProvider is the base class provider for the open API integration connector.
 */
public class ElasticsearchIntegrationProvider extends ConnectorProviderBase
{
    private static final String connectorTypeGUID          = "4cf65dbf-0808-4968-819b-6a49a9fe537a";
    private static final String connectorTypeQualifiedName = "Egeria:IntegrationConnector:ElasticsearchIntegrationProvider";
    private static final String connectorTypeDisplayName   = "Search Integration Connector";
    private static final String connectorTypeDescription   = "Connector used to connect to a Elasticsearch instance and store metadata for search operations";

    static final String TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "templateQualifiedName";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ElasticsearchIntegrationProvider()
    {
        super();

        super.setConnectorClassName(ElasticsearchIntegrationConnector.class.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
