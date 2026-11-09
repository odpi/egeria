/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.organization;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * BusinessCapabilityDepartmentsTabularDataSetProvider is the connector provider for the BusinessCapabilityDepartmentsTabularDataSetConnector
 * that lists the departments that deliver each business capability.
 */
public class BusinessCapabilityDepartmentsTabularDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = BusinessCapabilityDepartmentsTabularDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public BusinessCapabilityDepartmentsTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.BUSINESS_CAPABILITY_DEPARTMENTS_TABULAR_DATA_SET,
              connectorClassName,
              List.of(TabularDataSetConfigurationProperty.SERVER_NAME.name,
                      TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.name));
    }
}
