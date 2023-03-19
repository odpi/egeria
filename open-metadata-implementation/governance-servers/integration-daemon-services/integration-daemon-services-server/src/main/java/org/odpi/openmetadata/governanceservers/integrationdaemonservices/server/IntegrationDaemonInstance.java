/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorCacheMap;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.IntegrationDaemonThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * IntegrationDaemonInstance maintains the instance information needed to execute requests on behalf of
 * an integration daemon.  The integration daemon is running
 */
public class IntegrationDaemonInstance extends GovernanceServerServiceInstance
{
    private final IntegrationDaemonThread                integrationDaemonThread;
    private final Map<String, IntegrationServiceHandler> integrationServiceHandlers;
    private final Map<String, IntegrationGroupHandler>   integrationGroupHandlers;
    private final IntegrationConnectorCacheMap           integrationConnectorCacheMap;

    /**
     * Constructor where REST Services used.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId userId to use for local server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param integrationDaemonThread handler managing the threading for the active integration connectors in this server.
     * @param integrationServiceHandlers handler for all the active integration services in this server.
     */
    IntegrationDaemonInstance(String                                 serverName,
                              String                                 serviceName,
                              AuditLog                               auditLog,
                              String                                 localServerUserId,
                              int                                    maxPageSize,
                              IntegrationDaemonThread                integrationDaemonThread,
                              Map<String, IntegrationServiceHandler> integrationServiceHandlers,
                              Map<String, IntegrationGroupHandler>   integrationGroupHandlers,
                              IntegrationConnectorCacheMap           integrationConnectorCacheMap)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

        this.integrationDaemonThread = integrationDaemonThread;
        this.integrationServiceHandlers = integrationServiceHandlers;
        this.integrationGroupHandlers = integrationGroupHandlers;
        this.integrationConnectorCacheMap = integrationConnectorCacheMap;
    }


    /**
     * Return the list of all the integration service handlers for this integration daemon.
     *
     * @param serviceOperationName name of calling request
     * @return list of integration service handlers.
     * @throws PropertyServerException there are no integration services in this integration daemon
     */
    synchronized List<IntegrationServiceHandler> getAllIntegrationServiceHandlers(String serviceOperationName) throws PropertyServerException
    {
        if ((integrationServiceHandlers == null) || (integrationServiceHandlers.isEmpty()))
        {
            return null;
        }

        return new ArrayList<>(integrationServiceHandlers.values());
    }


    /**
     * Return the integration service instance requested on an integration daemon services request.
     *
     * @param serviceURLMarker identifier of integration service
     * @param serviceOperationName name of calling request
     * @return integration service handler.
     */
    synchronized IntegrationServiceHandler getIntegrationServiceHandler(String serviceURLMarker,
                                                                        String serviceOperationName) throws InvalidParameterException
    {
        final String nameParameterName = "serviceURLMarker";

        IntegrationServiceHandler handler = integrationServiceHandlers.get(serviceURLMarker);

        if (handler == null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_INTEGRATION_SERVICE.getMessageDefinition(serverName,
                                                                                                                                    serviceURLMarker),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                nameParameterName);
        }

        return handler;
    }


    /**
     * Retrieve all the definitions for the requested integration group from the Governance Engine OMAS
     * running in a metadata server.
     *
     * @param integrationGroupName qualifiedName of the requested integration group
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    void  refreshConfig(String integrationGroupName,
                        String serviceOperationName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String integrationGroupParameterName = "integrationGroupName";

        invalidParameterHandler.validateName(integrationGroupName, integrationGroupParameterName, serviceOperationName);

        if ((integrationGroupHandlers == null) || (integrationGroupHandlers.isEmpty()))
        {
            throw new PropertyServerException(IntegrationDaemonServicesErrorCode.NO_INTEGRATION_GROUPS.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        IntegrationGroupHandler handler = integrationGroupHandlers.get(integrationGroupName);

        if (handler == null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_GROUP_NAME.getMessageDefinition(serverName, integrationGroupName),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                integrationGroupParameterName);
        }

        handler.refreshConfig();
    }


    /**
     * Return a summary of the requested engine's status.
     *
     * @param integrationGroupName qualifiedName of the requested integration group
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    IntegrationGroupSummary getIntegrationGroupSummary(String integrationGroupName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           PropertyServerException
    {
        final String integrationGroupParameterName = "integrationGroupName";

        invalidParameterHandler.validateName(integrationGroupName, integrationGroupParameterName, serviceOperationName);

        if ((integrationGroupHandlers == null) || (integrationGroupHandlers.isEmpty()))
        {
            throw new PropertyServerException(IntegrationDaemonServicesErrorCode.NO_INTEGRATION_GROUPS.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        IntegrationGroupHandler handler = integrationGroupHandlers.get(integrationGroupName);

        if (handler == null)
        {
            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_GROUP_NAME.getMessageDefinition(serverName, integrationGroupName),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                integrationGroupParameterName);
        }

        return handler.getSummary();
    }


    /**
     * Return a summary of all the engine statuses for the integration daemon.
     *
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<IntegrationGroupSummary> getIntegrationGroupSummaries(String serviceOperationName) throws InvalidParameterException,
                                                                                                   PropertyServerException
    {
        if ((integrationGroupHandlers == null) || (integrationGroupHandlers.isEmpty()))
        {
            return null;
        }

        List<IntegrationGroupSummary> summaries = new ArrayList<>();

        for (String integrationGroupName : integrationGroupHandlers.keySet())
        {
            if (integrationGroupName != null)
            {
                summaries.add(this.getIntegrationGroupSummary(integrationGroupName, serviceOperationName));
            }
        }

        if (summaries.isEmpty())
        {
            return null;
        }

        return summaries;
    }
    

    /**
     * Shutdown the integration daemon
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "Server shutdown";

        /*
         * Shutdown the threads running the connectors.
         */
        if (integrationDaemonThread != null)
        {
            integrationDaemonThread.stop();
        }

        /*
         * This shuts down the connectors and stops any dedicated threads.
         */
        if (integrationConnectorCacheMap != null)
        {
            for (String connectorId : integrationConnectorCacheMap.getConnectorIds())
            {
                IntegrationConnectorHandler handler = integrationConnectorCacheMap.getHandlerByConnectorId(connectorId);
                if (handler != null)
                {
                    handler.shutdown(actionDescription);
                }
            }
        }

        super.shutdown();
    }
}
