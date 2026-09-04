/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.productfamily;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * DigitalProductFamilyDataSetCollectionProvider is the connector provider for the connector that presents the
 * tabular data sets of a digital product family's products as one collection.  Jacquard attaches this connector
 * to the asset it creates for each product family, naming the family in the connection's configuration
 * properties.
 */
public class DigitalProductFamilyDataSetCollectionProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = DigitalProductFamilyDataSetCollectionConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * connector implementation.
     */
    public DigitalProductFamilyDataSetCollectionProvider()
    {
        super(EgeriaOpenConnectorDefinition.DIGITAL_PRODUCT_FAMILY_TABULAR_DATA_SET_COLLECTION,
              connectorClassName,
              List.of(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName()));
    }
}
