/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;


/**
 * ValidValueSetListProvider is the connector provider for the ValidValueSetList connector that manages the list
 * of valid value sets as a tabular data source.
 */
public class ValidMetadataValueSetListProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = ValidMetadataValueSetListConnector.class.getName();

    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ValidMetadataValueSetListProvider()
    {
        super(EgeriaOpenConnectorDefinition.VALID_METADATA_VALUE_SET_LIST_TABULAR_DATA_SET,
              connectorClassName,
              null);
    }
}
