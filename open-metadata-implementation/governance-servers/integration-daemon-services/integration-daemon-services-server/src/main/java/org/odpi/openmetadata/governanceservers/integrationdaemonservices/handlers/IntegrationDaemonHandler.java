/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationServiceSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * IntegrationDaemonHandler is responsible for servicing requests for an integration daemon.
 */
public class IntegrationDaemonHandler
{
    private List<IntegrationConnectorHandler>      daemonConnectorHandlers = new ArrayList<>();

    private Map<String, IntegrationServiceHandler> integrationServiceInstances;
    private String serverName = null;

    public IntegrationDaemonHandler(List<IntegrationConnectorHandler> daemonConnectorHandlers) {}

    /**
     * Retrieve the status of each running integration service.
     *
     * @param userId calling user
     * @return list of integration services statuses
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public List<IntegrationServiceSummary> getIntegrationDaemonStatus(String userId) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        return null;
    }


    /**
     * Return the integration service instance requested on an integration service services request.
     *
     * @return list of integration service summaries.
     */
    synchronized List<IntegrationServiceSummary> getIntegrationServiceStatuses()
    {
        List<IntegrationServiceSummary> results = new ArrayList<>();

        if (integrationServiceInstances != null)
        {
            for (IntegrationServiceHandler  integrationServiceHandler : integrationServiceInstances.values())
            {
                if (integrationServiceHandler != null)
                {
                    //results.add(integrationServiceHandler.getSummary());
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }

    }


    public void refreshAllServices(String userId) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {

    }

    public void refreshService(String userId,
                               String serviceURLMarker,
                               String connectorName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String  methodName        = "getIntegrationService";
        final String  nameParameterName = "integrationServiceURLMarker";

        IntegrationDaemonHandler instance = null;

        if (instance == null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_INTEGRATION_SERVICE.getMessageDefinition(serverName,
                                                                                                                                    serviceURLMarker),
                                                this.getClass().getName(),
                                                methodName,
                                                nameParameterName);
        }
    }


    public void shutdown()
    {
        if (integrationServiceInstances != null)
        {
            for (IntegrationServiceHandler  handler : integrationServiceInstances.values())
            {
                if (handler != null)
                {
                    handler.shutdown();
                }
            }
        }
    }
}
