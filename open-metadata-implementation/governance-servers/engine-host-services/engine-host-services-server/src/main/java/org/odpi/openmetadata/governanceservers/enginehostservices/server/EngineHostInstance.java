/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.properties.GovernanceEngineSummary;
import org.odpi.openmetadata.governanceservers.enginehostservices.threads.EngineConfigurationRefreshThread;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * IntegrationDaemonInstance maintains the instance information needed to execute requests on behalf of
 * an integration daemon.  The integration daemon is running
 */
public class EngineHostInstance extends GovernanceServerServiceInstance
{
    private final EngineConfigurationRefreshThread     configurationRefreshThread;
    private final Map<String, GovernanceEngineHandler> governanceEngineHandlers;
    private final Map<String, List<String>>            serviceEngineNames;



    /**
     * Constructor where REST Services used.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls
     * @param localServerUserId userId to use for local server initiated actions
     * @param maxPageSize max number of results to return on single request
     * @param configurationRefreshThread handler managing the threading for the active integration connectors in this server
     * @param governanceEngineHandlers map from governance engine name to governance engine handler
     * @param serviceEngineNames map for engine service URL marker to governance engine qualified names
     */
    EngineHostInstance(String                                            serverName,
                       String                                            serviceName,
                       AuditLog                                          auditLog,
                       String                                            localServerUserId,
                       int                                               maxPageSize,
                       EngineConfigurationRefreshThread                  configurationRefreshThread,
                       Map<String, GovernanceEngineHandler>              governanceEngineHandlers,
                       Map<String, List<String>>                         serviceEngineNames)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

        this.configurationRefreshThread = configurationRefreshThread;
        this.governanceEngineHandlers = governanceEngineHandlers;
        this.serviceEngineNames = serviceEngineNames;
    }


    /**
     * Retrieve all the definitions for the requested governance engine from the Governance Engine OMAS
     * running in a metadata server.
     *
     * @param governanceEngineName qualifiedName of the requested governance engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    void  refreshConfig(String governanceEngineName,
                        String serviceOperationName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String governanceEngineParameterName = "governanceEngineName";

        invalidParameterHandler.validateName(governanceEngineName, governanceEngineParameterName, serviceOperationName);

        if ((governanceEngineHandlers == null) || (governanceEngineHandlers.isEmpty()))
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.NO_GOVERNANCE_ENGINES.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        GovernanceEngineHandler handler = governanceEngineHandlers.get(governanceEngineName);

        if (handler == null)
        {
            throw new InvalidParameterException(EngineHostServicesErrorCode.UNKNOWN_ENGINE_NAME.getMessageDefinition(serverName, governanceEngineName),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                governanceEngineParameterName);
        }

        handler.refreshConfig();
    }


    /**
     * Return a summary of the requested engine's status.
     *
     * @param governanceEngineName qualifiedName of the requested governance engine
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    GovernanceEngineSummary getGovernanceEngineSummary(String governanceEngineName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           PropertyServerException
    {
        final String governanceEngineParameterName = "governanceEngineName";

        invalidParameterHandler.validateName(governanceEngineName, governanceEngineParameterName, serviceOperationName);

        if ((governanceEngineHandlers == null) || (governanceEngineHandlers.isEmpty()))
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.NO_GOVERNANCE_ENGINES.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        GovernanceEngineHandler handler = governanceEngineHandlers.get(governanceEngineName);

        if (handler == null)
        {
            throw new InvalidParameterException(EngineHostServicesErrorCode.UNKNOWN_ENGINE_NAME.getMessageDefinition(serverName, governanceEngineName),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                governanceEngineParameterName);
        }

        return handler.getSummary();
    }


    /**
     * Return a summary of all the engine statuses for the engine service.
     *
     * @param serviceURLMarker URL identifier of the Engine Service
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String serviceURLMarker,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   PropertyServerException
    {
        final String nameParameterName = "serviceURLMarker";

        invalidParameterHandler.validateName(serviceURLMarker, nameParameterName, serviceOperationName);

        List<String> governanceEngineNames = serviceEngineNames.get(serviceURLMarker);

        if (governanceEngineNames == null)
        {
            throw new InvalidParameterException(EngineHostServicesErrorCode.UNKNOWN_ENGINE_SERVICE.getMessageDefinition(serverName, serviceURLMarker),
                                                this.getClass().getName(),
                                                serviceOperationName,
                                                nameParameterName);
        }

        if (governanceEngineNames.isEmpty())
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.NO_ENGINES_FOR_SERVICE.getMessageDefinition(serviceOperationName, serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        List<GovernanceEngineSummary> summaries = new ArrayList<>();

        for (String governanceEngineName : governanceEngineNames)
        {
            if (governanceEngineName != null)
            {
                summaries.add(this.getGovernanceEngineSummary(governanceEngineName, serviceOperationName));
            }
        }

        if (summaries.isEmpty())
        {
            return null;
        }

        return summaries;
    }


    /**
     * Return a summary of all the engine statuses for the engine host.
     *
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<GovernanceEngineSummary> getGovernanceEngineSummaries(String serviceOperationName) throws InvalidParameterException,
                                                                                                   PropertyServerException
    {
        if ((governanceEngineHandlers == null) || (governanceEngineHandlers.isEmpty()))
        {
            throw new PropertyServerException(EngineHostServicesErrorCode.NO_GOVERNANCE_ENGINES.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
        }

        List<GovernanceEngineSummary> summaries = new ArrayList<>();

        for (String governanceEngineName : governanceEngineHandlers.keySet())
        {
            if (governanceEngineName != null)
            {
                summaries.add(this.getGovernanceEngineSummary(governanceEngineName, serviceOperationName));
            }
        }

        if (summaries.isEmpty())
        {
            return null;
        }

        return summaries;
    }




    /**
     * Shutdown the engines
     */
    @Override
    public void shutdown()
    {
        if ((governanceEngineHandlers != null) && (! governanceEngineHandlers.isEmpty()))
        {
            for (GovernanceEngineHandler handler : governanceEngineHandlers.values())
            {
                if (handler != null)
                {
                    handler.terminate();
                }
            }
        }


        /*
         * Shutdown the thread managing the configuration refresh.
         */
        configurationRefreshThread.stop();

        super.shutdown();
    }

}
