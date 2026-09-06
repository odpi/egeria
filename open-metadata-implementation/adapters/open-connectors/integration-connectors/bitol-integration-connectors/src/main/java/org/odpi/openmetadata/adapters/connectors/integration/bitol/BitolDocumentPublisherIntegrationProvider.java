/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;

/**
 * BitolDocumentPublisherIntegrationProvider is the connector provider for the BitolDocumentPublisherIntegrationConnector.
 */
public class BitolDocumentPublisherIntegrationProvider extends IntegrationConnectorProvider
{
    /**
     * The name of the catalog target that identifies a digital product catalog whose products are published.
     */
    public static final String CATALOG_TARGET_NAME = "productCatalog";

    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.integration.bitol.BitolDocumentPublisherIntegrationConnector";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BitolDocumentPublisherIntegrationProvider()
    {
        super(EgeriaOpenConnectorDefinition.BITOL_DOCUMENT_PUBLISHER_INTEGRATION_CONNECTOR,
              connectorClassName,
              null);

        CatalogTargetType catalogTargetType = new CatalogTargetType();

        catalogTargetType.setName(CATALOG_TARGET_NAME);
        catalogTargetType.setTypeName(OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName);

        super.catalogTargets = new ArrayList<>();
        super.catalogTargets.add(catalogTargetType);
    }
}
