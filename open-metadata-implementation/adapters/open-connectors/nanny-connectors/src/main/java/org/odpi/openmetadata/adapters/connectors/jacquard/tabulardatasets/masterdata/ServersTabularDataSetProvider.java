/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * ServersTabularDataSetProvider is the connector provider for the ServersTabularDataSetConnector
 * that lists the software servers catalogued in open metadata.
 */
public class ServersTabularDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = ServersTabularDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ServersTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.SERVERS_TABULAR_DATA_SET,
              connectorClassName,
              List.of(TabularDataSetConfigurationProperty.SERVER_NAME.name,
                      TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.name));
    }
}
