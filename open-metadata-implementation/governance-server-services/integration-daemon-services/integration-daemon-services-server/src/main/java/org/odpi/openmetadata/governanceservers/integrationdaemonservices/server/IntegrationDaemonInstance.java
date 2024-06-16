/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorCacheMap;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationConnectorReport;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * IntegrationDaemonInstance maintains the instance information needed to execute requests on behalf of
 * an integration daemon.  The integration daemon is running
 */
public class IntegrationDaemonInstance extends GovernanceServerServiceInstance
{
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
     * @param integrationServiceHandlers handler for all the active integration services in this server.
     */
    IntegrationDaemonInstance(String                                 serverName,
                              String                                 serviceName,
                              AuditLog                               auditLog,
                              String                                 localServerUserId,
                              int                                    maxPageSize,
                              Map<String, IntegrationServiceHandler> integrationServiceHandlers,
                              Map<String, IntegrationGroupHandler>   integrationGroupHandlers,
                              IntegrationConnectorCacheMap           integrationConnectorCacheMap)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

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
     * Return the list of all the integration group handlers for this integration daemon.
     *
     * @param serviceOperationName name of calling request
     * @return list of integration service handlers.
     * @throws PropertyServerException there are no integration groups in this integration daemon
     */
    synchronized List<IntegrationGroupHandler> getAllIntegrationGroupHandlers(String serviceOperationName) throws PropertyServerException
    {
        if ((integrationGroupHandlers == null) || (integrationGroupHandlers.isEmpty()))
        {
            return null;
        }

        return new ArrayList<>(integrationGroupHandlers.values());
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
    void refreshIntegrationGroupConfig(String integrationGroupName,
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
     * Retrieve the configuration properties of the named connector.
     *
     * @param connectorName name of a specific connector
     *
     * @return property map
     *
     * @throws InvalidParameterException the connector name is not recognized
     */
    public Map<String, Object> getConfigurationProperties(String connectorName) throws InvalidParameterException
    {
        final String   methodName = "getConfigurationProperties";
        final String   connectorNameParameterName = "connectorName";

        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);
        if (connectorHandler != null)
        {
            return connectorHandler.getConfigurationProperties();
        }

        final String parameterName = "connectorName";
        final String actionDescription = "Retrieve configuration properties";

        throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName, serverName),
                                            this.getClass().getName(),
                                            actionDescription,
                                            parameterName);
    }


    /**
     * Update the configuration properties of a specific named connector.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param isMergeUpdate should the properties be merged into the existing properties or replace them
     * @param configurationProperties new configuration properties
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void updateConfigurationProperties(String              userId,
                                              String              connectorName,
                                              boolean             isMergeUpdate,
                                              Map<String, Object> configurationProperties) throws InvalidParameterException
    {
        final String   methodName = "updateConfigurationProperties";
        final String   connectorNameParameterName = "connectorName";

        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);
        if (connectorHandler != null)
        {
            connectorHandler.updateConfigurationProperties(userId, methodName, isMergeUpdate, configurationProperties);
        }
        else
        {
            final String actionDescription = "Update connector configuration properties REST API call";
            final String parameterName = "connectorName";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                               serverName),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }
    }


    /**
     * Update the endpoint network address for a specific integration connector.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param networkAddress name of a specific connector or null for all connectors and the properties to change
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */

    public void updateEndpointNetworkAddress(String userId,
                                             String connectorName,
                                             String networkAddress)  throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   methodName = "updateEndpointNetworkAddress";
        final String   connectorNameParameterName = "connectorName";

        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);
        if (connectorHandler != null)
        {
            connectorHandler.updateEndpointNetworkAddress(userId, methodName, networkAddress);
        }
        else
        {
            final String actionDescription = "Update connector endpoint network address REST API call";
            final String parameterName = "connectorName";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                               serverName),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }
    }


    /**
     * Update the connection for a specific integration connector.
     *
     * @param userId calling user
     * @param connectorName name of a specific connector
     * @param connection new connection object
     *
     * @throws InvalidParameterException the connector name is not recognized
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public  void updateConnectorConnection(String     userId,
                                           String     connectorName,
                                           Connection connection) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "updateConnectorConnection";
        final String   connectorNameParameterName = "connectorName";

        invalidParameterHandler.validateName(connectorName, connectorNameParameterName, methodName);

        IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);
        if (connectorHandler != null)
        {
            connectorHandler.updateConnectorConnection(userId, methodName, connection);
        }
        else
        {
            final String actionDescription = "Update connector connection REST API call";
            final String parameterName = "connectorName";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                               serverName),
                                                this.getClass().getName(),
                                                actionDescription,
                                                parameterName);
        }
    }



    /**
     * Retrieve all the connectors.
     */
    public List<IntegrationConnectorReport> getConnectorReports()
    {
        List<IntegrationConnectorReport> connectorReports = new ArrayList<>();

        for (String connectorId : integrationConnectorCacheMap.getConnectorIds())
        {
            if (connectorId != null)
            {
                IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorId(connectorId);

                if (connectorHandler != null)
                {
                    IntegrationConnectorReport connectorReport = new IntegrationConnectorReport();

                    connectorReport.setConnectorId(connectorHandler.getIntegrationConnectorId());
                    connectorReport.setConnectorName(connectorHandler.getIntegrationConnectorName());
                    connectorReport.setConnectorStatus(connectorHandler.getIntegrationConnectorStatus());
                    connectorReport.setConnection(connectorHandler.getConnection());
                    connectorReport.setConnectorInstanceId(connectorHandler.getIntegrationConnectorInstanceId());
                    connectorReport.setFailingExceptionMessage(connectorHandler.getFailingExceptionMessage());
                    connectorReport.setStatistics(connectorHandler.getStatistics());
                    connectorReport.setLastStatusChange(connectorHandler.getLastStatusChange());
                    connectorReport.setLastRefreshTime(connectorHandler.getLastRefreshTime());
                    connectorReport.setMinMinutesBetweenRefresh(connectorHandler.getMinMinutesBetweenRefresh());

                    connectorReports.add(connectorReport);
                }
            }
        }

        return connectorReports;
    }


    /**
     * Refresh all the connectors, or a specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void refreshConnector(String connectorName) throws InvalidParameterException
    {
        final String methodName = "refreshConnector";

        if (connectorName == null)
        {
            for (String connectorId : integrationConnectorCacheMap.getConnectorIds())
            {
                if (connectorId != null)
                {
                    IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorId(connectorId);

                    if (connectorHandler != null)
                    {
                        connectorHandler.refreshConnector(methodName, false);
                    }
                }
            }
        }
        else
        {
            IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);

            if (connectorHandler != null)
            {
                connectorHandler.refreshConnector(methodName, false);
            }
            else
            {
                final String parameterName = "connectorName";

                throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                                   serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
        }
    }


    /**
     * Restart all the connectors, or a specific connector if a connector name is supplied.
     *
     * @param connectorName name of a specific connector or null for all connectors
     * @throws InvalidParameterException the connector name is not recognized
     */
    public void restartConnector(String connectorName) throws InvalidParameterException
    {
        final String methodName = "restartConnector";

        if (connectorName == null)
        {
            for (String connectorId : integrationConnectorCacheMap.getConnectorIds())
            {
                if (connectorId != null)
                {
                    IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorId(connectorId);

                    if (connectorHandler != null)
                    {
                        connectorHandler.reinitializeConnector(methodName);
                    }
                }
            }
        }
        else
        {
            IntegrationConnectorHandler connectorHandler = integrationConnectorCacheMap.getHandlerByConnectorName(connectorName);

            if (connectorHandler != null)
            {
                connectorHandler.reinitializeConnector(methodName);
            }
            else
            {
                final String parameterName = "connectorName";

                throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_CONNECTOR_NAME.getMessageDefinition(connectorName,
                                                                                                                                   serverName),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    parameterName);
            }
        }
    }


    /**
     * Shutdown the integration daemon
     */
    @Override
    public void shutdown()
    {
        final String actionDescription = "Server shutdown";

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
