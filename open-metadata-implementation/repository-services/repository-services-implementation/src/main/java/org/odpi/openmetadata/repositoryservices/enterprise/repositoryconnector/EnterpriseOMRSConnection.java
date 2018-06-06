/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector;

import org.odpi.openmetadata.frameworks.connectors.properties.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectorType;

/**
 * EnterpriseOMRSConnection provides a valid connection for the EnterpriseOMRSConnector.
 */
public class EnterpriseOMRSConnection extends Connection
{
    final ConnectorType enterpriseConnectorType = new ConnectorType(null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    EnterpriseOMRSConnectorProvider.class.getName());


    /**
     * Default Constructor that sets up the connector
     */
    public EnterpriseOMRSConnection()
    {
        super(null);
        super.connectorType = enterpriseConnectorType;
    }
}
