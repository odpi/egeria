/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * EnterpriseOMRSConnection provides a valid connection for the EnterpriseOMRSConnector.
 */
public class EnterpriseOMRSConnection extends ConnectionProperties
{
    private static final long    serialVersionUID = 1L;

    /**
     * Default Constructor that sets up the connector
     */
    public EnterpriseOMRSConnection()
    {
        super(new Connection());

        Connection    connectionBean = super.getConnectionBean();
        ConnectorType connectorType = new ConnectorType();

        connectorType.setConnectorProviderClassName(EnterpriseOMRSConnectorProvider.class.getName());
        connectionBean.setConnectorType(connectorType);
    }
}
