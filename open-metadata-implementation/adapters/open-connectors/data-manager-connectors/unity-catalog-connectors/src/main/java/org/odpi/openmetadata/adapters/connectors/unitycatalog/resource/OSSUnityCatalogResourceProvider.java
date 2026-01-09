/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.unitycatalog.resource;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

public class OSSUnityCatalogResourceProvider extends OpenConnectorProviderBase
{
    /**
     * Class of the connector.
     */
    private static final String connectorClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceConnector";


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OSSUnityCatalogResourceProvider()
    {
        super(EgeriaOpenConnectorDefinition.OSS_UNITY_CATALOG_RESOURCE_CONNECTOR,
              connectorClassName,
              null);
    }
}
