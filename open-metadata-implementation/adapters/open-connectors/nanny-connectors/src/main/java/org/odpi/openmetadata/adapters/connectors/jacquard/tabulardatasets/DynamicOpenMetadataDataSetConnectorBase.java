/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets;


import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

/**
 * DynamicOpenMetadataDataSetConnector is the base class for the dynamic tabular data set to allow the provider
 * to pass the product definition to the open metadata repository.
 */
public abstract class DynamicOpenMetadataDataSetConnectorBase extends OpenMetadataRootDataSetConnectorBase
{
    protected String identifierPropertyValue = null;
    protected String canonicalName           = null;
    protected String description             = null;

    /**
     * Constructor used to set up the name of this connector (supplied by the subclasses).
     *
     * @param connectorName name of the connector
     */
    public DynamicOpenMetadataDataSetConnectorBase(String connectorName)
    {
        super(connectorName);
    }


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector
     */
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);

        identifierPropertyValue = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.IDENTIFIER_PROPERTY_VALUE.getName(), connectionBean.getConfigurationProperties());
        canonicalName = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.CANONICAL_NAME.getName(), connectionBean.getConfigurationProperties());
        description = super.getStringConfigurationProperty(TabularDataSetConfigurationProperty.PRODUCT_DESCRIPTION.getName(), connectionBean.getConfigurationProperties());

    }
}
