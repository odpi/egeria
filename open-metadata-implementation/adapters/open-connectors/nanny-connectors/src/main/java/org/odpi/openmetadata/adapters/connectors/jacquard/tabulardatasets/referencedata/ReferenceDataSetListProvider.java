/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.ReferenceDataConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * ValidValueSetListProvider is the connector provider for the ValidValueSetList connector that manages the list
 * of valid value sets as a tabular data source.
 */
public class ReferenceDataSetListProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName  = ReferenceDataSetListConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ReferenceDataSetListProvider()
    {
        super(EgeriaOpenConnectorDefinition.REFERENCE_DATA_LIST_TABULAR_DATA_SET,
              connectorClassName,
              List.of(ReferenceDataConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.name,
                      ReferenceDataConfigurationProperty.PRODUCT_DESCRIPTION.name,
                      ReferenceDataConfigurationProperty.CANONICAL_NAME.name,
                      ReferenceDataConfigurationProperty.STARTING_ELEMENT_GUID.name));
    }
}
