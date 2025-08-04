/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

/**
 * EnterpriseOMRSConnection provides a valid connection for the EnterpriseOMRSConnector.
 */
public class EnterpriseOMRSConnection extends Connection
{
    /**
     * Default Constructor that sets up the connector
     */
    public EnterpriseOMRSConnection()
    {
        super(new Connection());

        Connection    connectionBean = this;
        ConnectorType connectorType = new ConnectorType();

        connectorType.setConnectorProviderClassName(EnterpriseOMRSConnectorProvider.class.getName());
        connectionBean.setConnectorType(connectorType);
    }
}
