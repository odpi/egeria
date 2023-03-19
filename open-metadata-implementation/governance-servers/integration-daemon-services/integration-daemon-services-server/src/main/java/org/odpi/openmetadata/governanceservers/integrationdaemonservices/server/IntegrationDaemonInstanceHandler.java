/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.properties.IntegrationGroupSummary;

import java.util.List;

/**
 * IntegrationDaemonInstanceHandler retrieves information from the instance map for the
 * integration daemon instances.  The instance map is thread-safe.  Instances are added
 * and removed by the IntegrationDaemonOperationalServices class.
 */
public class IntegrationDaemonInstanceHandler extends GovernanceServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    public IntegrationDaemonInstanceHandler()
    {
        super(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName());
    }


    /**
     * Retrieve the all the integration service handlers for the requested integration daemon.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<IntegrationServiceHandler> getAllIntegrationServiceHandlers(String userId,
                                                                     String serverName,
                                                                     String serviceOperationName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getAllIntegrationServiceHandlers(serviceOperationName);
        }

        return null;
    }


    /**
     * Retrieve the specific handler for the requested integration service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceURLMarker marker that identifies the called service in the URL
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public IntegrationServiceHandler getIntegrationServiceHandler(String userId,
                                                                  String serverName,
                                                                  String serviceURLMarker,
                                                                  String serviceOperationName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getIntegrationServiceHandler(serviceURLMarker, serviceOperationName);
        }

        return null;
    }


    /**
     * Retrieve the specific context manager for the requested integration service.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceURLMarker marker that identifies the called service in the URL
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    public IntegrationContextManager getIntegrationServiceContextManager(String userId,
                                                                         String serverName,
                                                                         String serviceURLMarker,
                                                                         String serviceOperationName) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            IntegrationServiceHandler handler = instance.getIntegrationServiceHandler(serviceURLMarker, serviceOperationName);

            if (handler != null)
            {
                return handler.getContextManager();
            }
        }

        return null;
    }


    /**
     * Retrieve all the definitions for the requested integration group from the Governance Engine OMAS.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param integrationGroupName qualifiedName of the requested integration group
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    void  refreshConfig(String userId,
                        String serverName,
                        String integrationGroupName,
                        String serviceOperationName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            instance.refreshConfig(integrationGroupName, serviceOperationName);
        }
    }


    /**
     * Return a summary of the requested engine's status.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param integrationGroupName qualifiedName of the requested integration group
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    IntegrationGroupSummary getIntegrationGroupSummary(String userId,
                                                       String serverName,
                                                       String integrationGroupName,
                                                       String serviceOperationName) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getIntegrationGroupSummary(integrationGroupName, serviceOperationName);
        }

        return null;
    }


    /**
     * Return a summary of all the engine statuses for the integration daemon.
     *
     * @param userId calling user
     * @param serverName name of the server tied to the request
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     *
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    List<IntegrationGroupSummary> getIntegrationGroupSummaries(String userId,
                                                               String serverName,
                                                               String serviceOperationName) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        IntegrationDaemonInstance instance = (IntegrationDaemonInstance)super.getServerServiceInstance(userId, serverName, serviceOperationName);

        if (instance != null)
        {
            return instance.getIntegrationGroupSummaries(serviceOperationName);
        }

        return null;
    }
}
