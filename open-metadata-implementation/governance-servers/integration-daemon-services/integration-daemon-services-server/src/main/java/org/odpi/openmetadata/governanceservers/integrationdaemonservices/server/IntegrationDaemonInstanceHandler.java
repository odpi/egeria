/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.commonservices.multitenant.GovernanceServerServiceInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.integrationservices.catalogintegrator.contextmanager.CatalogIntegratorContextManager;
import org.odpi.openmetadata.integrationservices.database.contextmanager.DatabaseIntegratorContextManager;
import org.odpi.openmetadata.integrationservices.files.contextmanager.FilesIntegratorContextManager;
import org.odpi.openmetadata.integrationservices.lineageintegrator.contextmanager.LineageIntegratorContextManager;
import org.odpi.openmetadata.integrationservices.organizationintegrator.contextmanager.OrganizationIntegratorContextManager;

import java.util.List;

/**
 * IntegrationDaemonInstanceHandler retrieves information from the instance map for the
 * integration daemon instances.  The instance map is thread-safe.  Instances are added
 * and removed by the IntegrationDaemonOperationalServices class.
 */
class IntegrationDaemonInstanceHandler extends GovernanceServerServiceInstanceHandler
{
    /**
     * Default constructor registers the access service
     */
    IntegrationDaemonInstanceHandler()
    {
        super(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName());

        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.CATALOG_INTEGRATOR_OMIS,
                                                              CatalogIntegratorContextManager.class.getName());
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.DATABASE_INTEGRATOR_OMIS,
                                                              DatabaseIntegratorContextManager.class.getName());
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.FILES_INTEGRATOR_OMIS,
                                                              FilesIntegratorContextManager.class.getName());
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS,
                                                              LineageIntegratorContextManager.class.getName());
        IntegrationServiceRegistry.registerIntegrationService(IntegrationServiceDescription.ORGANIZATION_INTEGRATOR_OMIS,
                                                              OrganizationIntegratorContextManager.class.getName());
    }


    /**
     * Retrieve the all of the integration service handlers for the requested integration daemon.
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
     * @param serviceOperationName name of the REST API call (typically the top-level methodName)
     * @return handler for use by the requested instance
     * @throws InvalidParameterException no available instance for the requested server
     * @throws UserNotAuthorizedException user does not have access to the requested server
     * @throws PropertyServerException the service name is not known - indicating a logic error
     */
    IntegrationServiceHandler getIntegrationServiceHandler(String userId,
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
}
