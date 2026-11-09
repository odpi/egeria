/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.OpenConnectorProviderBase;

import java.util.List;


/**
 * ZoneMembershipTotalsTabularDataSetProvider is the connector provider for the ZoneMembershipTotalsTabularDataSetConnector
 * that lists the governance zones and the total count of their members.
 */
public class ZoneMembershipTotalsTabularDataSetProvider extends OpenConnectorProviderBase
{
    /*
     * Class of the connector.
     */
    private static final String connectorClassName = ZoneMembershipTotalsTabularDataSetConnector.class.getName();


    /**
     * Constructor used to initialize the ConnectorProvider with the Java class name of the specific
     * store implementation.
     */
    public ZoneMembershipTotalsTabularDataSetProvider()
    {
        super(EgeriaOpenConnectorDefinition.ZONE_MEMBERSHIP_TOTALS_TABULAR_DATA_SET,
              connectorClassName,
              List.of(TabularDataSetConfigurationProperty.SERVER_NAME.name,
                      TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.name));
    }
}
