/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.opengovernance.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextClientBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.RegisteredIntegrationConnectorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.IntegrationGroupProperties;

import java.util.List;
import java.util.Map;

/**
 * Provides the methods to create, query and maintain the open metadata that controls running connectors.
 */
public class ConnectorConfigClient extends ConnectorContextClientBase
{
    private final GovernanceConfiguration governanceConfiguration;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param governanceConfigurationClient client to access open metadata
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public ConnectorConfigClient(ConnectorContextBase    parentContext,
                                 String                  localServerName,
                                 String                  localServiceName,
                                 String                  connectorUserId,
                                 String                  connectorGUID,
                                 String                  externalSourceGUID,
                                 String                  externalSourceName,
                                 GovernanceConfiguration governanceConfigurationClient,
                                 AuditLog                auditLog,
                                 int                     maxPageSize)
    {
        super(parentContext,
              localServerName,
              localServiceName,
              connectorUserId,
              connectorGUID,
              externalSourceGUID,
              externalSourceName,
              auditLog,
              maxPageSize);

        this.governanceConfiguration = governanceConfigurationClient;
    }


}
