/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * BasicFilesMonitorIntegrationProviderBase is the base class provider for the basic files integration connectors.
 */
class BasicFilesMonitorIntegrationProviderBase extends ConnectorProviderBase
{
    static final String TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY = "templateQualifiedName";
    static final String ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY    = "allowCatalogDelete";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     *
     * @param connectorTypeGUID the unique identifier for this connector
     * @param connectorTypeQualifiedName the unique name for this connector type
     * @param connectorTypeDisplayName the printable name for this connector type
     * @param connectorTypeDescription the description of this connector type
     */
    BasicFilesMonitorIntegrationProviderBase(String   connectorTypeGUID,
                                             String   connectorTypeQualifiedName,
                                             String   connectorTypeDisplayName,
                                             String   connectorTypeDescription,
                                             Class<?> connectorClass)
    {
        super();

        super.setConnectorClassName(connectorClass.getName());

        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeQualifiedName);
        connectorType.setDisplayName(connectorTypeDisplayName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedConfigurationProperties = new ArrayList<>();
        recognizedConfigurationProperties.add(TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY);
        recognizedConfigurationProperties.add(ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY);

        connectorType.setRecognizedConfigurationProperties(recognizedConfigurationProperties);

        super.connectorTypeBean = connectorType;
    }
}
