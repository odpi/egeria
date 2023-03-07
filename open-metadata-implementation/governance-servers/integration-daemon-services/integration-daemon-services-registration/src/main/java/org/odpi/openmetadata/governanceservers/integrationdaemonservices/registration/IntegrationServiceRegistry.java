/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;

import java.util.*;

/**
 * IntegrationServiceRegistry maintains the list of registered integration services.
 */
public class IntegrationServiceRegistry
{
    private static final Map<String, IntegrationServiceDescription> serviceDescriptions    = new HashMap<>();
    private static final Map<String, String>                        serviceContextManagers = new HashMap<>();

    public static void registerIntegrationService(IntegrationServiceDescription  serviceDescription,
                                                  String                         contextManagerClass)
    {
        serviceDescriptions.put(serviceDescription.getIntegrationServiceURLMarker(), serviceDescription);
        serviceContextManagers.put(serviceDescription.getIntegrationServiceURLMarker(), contextManagerClass);
    }


    /**
     * Retrieve a partially filled configuration properties object for an integration service.  It needs the connections for the
     * integration connectors to be added.
     *
     * @param serviceURLMarker URL marker to identify the service
     * @param serverName and of server being configured
     * @param methodName calling method
     * @return partially filled configuration for the named integration service
     * @throws InvalidParameterException the service URL marker is not recognized
     */
    public static IntegrationServiceConfig getIntegrationServiceConfig(String serviceURLMarker,
                                                                       String serverName,
                                                                       String methodName) throws InvalidParameterException
    {
        IntegrationServiceDescription serviceDescription = serviceDescriptions.get(serviceURLMarker);
        String                        contextManagerClass = serviceContextManagers.get(serviceURLMarker);

        if ((serviceDescription != null) && (contextManagerClass != null))
        {
            IntegrationServiceConfig config = new IntegrationServiceConfig();

            config.setIntegrationServiceId(serviceDescription.getIntegrationServiceCode());
            config.setIntegrationServiceDevelopmentStatus(serviceDescription.getIntegrationServiceDevelopmentStatus());
            config.setIntegrationServiceName(serviceDescription.getIntegrationServiceName());
            config.setIntegrationServiceFullName(serviceDescription.getIntegrationServiceFullName());
            config.setIntegrationServiceURLMarker(serviceDescription.getIntegrationServiceURLMarker());
            config.setIntegrationServiceDescription(serviceDescription.getIntegrationServiceDescription());
            config.setIntegrationServiceWiki(serviceDescription.getIntegrationServiceWiki());
            config.setIntegrationServicePartnerOMAS(serviceDescription.getIntegrationServicePartnerOMAS());
            config.setDefaultPermittedSynchronization(serviceDescription.getDefaultPermittedSynchronization());

            config.setIntegrationServiceOperationalStatus(ServiceOperationalStatus.ENABLED);
            config.setIntegrationServiceContextManagerClass(contextManagerClass);

            return config;
        }
        else
        {
            final String actionDescription = "getIntegrationServiceConfig";

            throw new InvalidParameterException(IntegrationDaemonServicesErrorCode.UNKNOWN_INTEGRATION_SERVICE.getMessageDefinition(serviceURLMarker,
                                                                                                                                    serverName),
                                                IntegrationServiceRegistry.class.getName(),
                                                actionDescription,
                                                methodName);
        }
    }


    public static List<RegisteredOMAGService> getRegisteredIntegrationServices()
    {
        List<RegisteredOMAGService> response = new ArrayList<>();

        /*
         * Get the list of Implementation Services implemented in this server.
         */
        Collection<IntegrationServiceDescription> integrationServiceDescriptions = serviceDescriptions.values();

        /*
         * Set up the available access services.
         */
        if (! integrationServiceDescriptions.isEmpty())
        {
            for (IntegrationServiceDescription serviceDescription : integrationServiceDescriptions)
            {
                if (serviceDescription != null)
                {
                    RegisteredOMAGService service = new RegisteredOMAGService();

                    service.setServiceId(serviceDescription.getIntegrationServiceCode());
                    service.setServiceName(serviceDescription.getIntegrationServiceFullName());
                    service.setServiceDevelopmentStatus(serviceDescription.getIntegrationServiceDevelopmentStatus());
                    service.setServiceURLMarker(serviceDescription.getIntegrationServiceURLMarker());
                    service.setServiceDescription(serviceDescription.getIntegrationServiceDescription());
                    service.setServiceWiki(serviceDescription.getIntegrationServiceWiki());

                    response.add(service);
                }
            }
        }

        return response;
    }
}
