/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.openmetadatatypes;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;


/**
 * OpenMetadataTypesDataSetProvider is the connector provider for the OpenMetadataTypesDataSet connector that manages the members of
 * a valid values set as a tabular data set.
 */
public class OpenMetadataTypesDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName  = OpenMetadataTypesDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public OpenMetadataTypesDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.OPEN_METADATA_TYPES_LIST_TABULAR_DATA_SET, connectorClassName, null);
    }
}
