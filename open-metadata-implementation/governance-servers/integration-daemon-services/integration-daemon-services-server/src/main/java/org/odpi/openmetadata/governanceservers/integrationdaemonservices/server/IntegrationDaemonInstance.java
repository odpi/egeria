/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
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
    private IntegrationDaemonThread                integrationDaemonThread;
    private Map<String, IntegrationServiceHandler> integrationServiceHandlers;


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
                              Map<String, IntegrationServiceHandler> integrationServiceHandlers)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

        this.integrationDaemonThread = integrationDaemonThread;
        this.integrationServiceHandlers = integrationServiceHandlers;
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
            throw new PropertyServerException(IntegrationDaemonServicesErrorCode.NO_INTEGRATION_SERVICES.getMessageDefinition(serverName),
                                              this.getClass().getName(),
                                              serviceOperationName);
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
     * Shutdown the integration services
     */
    @Override
    public void shutdown()
    {
        if ((integrationServiceHandlers != null) && (! integrationServiceHandlers.isEmpty()))
        {
            for (IntegrationServiceHandler handler : integrationServiceHandlers.values())
            {
                if (handler != null)
                {
                    /*
                     * This shuts down the connectors
                     */
                    handler.shutdown();
                }
            }
        }

        /*
         * Shutdown the threads running the connectors.
         */
        if (integrationDaemonThread != null)
        {
            integrationDaemonThread.stop();
        }

        super.shutdown();
    }
}
