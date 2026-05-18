/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;


/**
 * OpenMetadataPropertiesDataSetProvider is the connector provider for the OpenMetadataPropertiesDataSet connector
 * that manages the list of open metadata properties as a tabular data source.
 */
public class OpenMetadataPropertiesDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = OpenMetadataAttributesForTypesDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OpenMetadataPropertiesDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.PROPERTIES_LIST_TABULAR_DATA_SET, connectorClassName, null);
    }
}
