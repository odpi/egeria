/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.commonservices.ffdc.rest.RegisteredOMAGService;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * IntegrationServiceRegistry maintains the list of registered integration services and the mapping to the specialized context manager/integration
 * connector APIs.
 */
public class IntegrationServiceRegistry
{
    private static final Logger                                     log                          = LoggerFactory.getLogger(IntegrationServiceRegistry.class);
    private static final Map<String, IntegrationServiceDescription> serviceDescriptions          = new HashMap<>();
    private static final Map<String, Class<?>>                      serviceContextManagerClasses = new HashMap<>();
    private static final Map<Class<?>, String>                      specializedAPIToServiceMap   = new HashMap<>();


    /**
     * Register an Open Metadata Integration service (OMIS) with the OMAG Server Platform.  This call is triggered when the OMIS is
     * loaded into the OMAG Server Platform as part of the Spring Component Scan.
     *
     * @param serviceDescription description of the OMIS
     * @param contextManagerClassName name of the class that implements a specialized context for the connectors.
     * @param connectorClassName name of the specialized connector interface that supports the specialized context.  This is implemented by the
     *                       integration connector and links it to a specific integration service.
     */
    public static void registerIntegrationService(IntegrationServiceDescription  serviceDescription,
                                                  String                         contextManagerClassName,
                                                  String                         connectorClassName)
    {
        try
        {
            Class<?> contextManagerClass = Class.forName(contextManagerClassName);
            Class<?> connectorClass      = Class.forName(connectorClassName);

            serviceDescriptions.put(serviceDescription.getIntegrationServiceURLMarker(), serviceDescription);
            serviceContextManagerClasses.put(serviceDescription.getIntegrationServiceURLMarker(), contextManagerClass);
            specializedAPIToServiceMap.put(connectorClass, serviceDescription.getIntegrationServiceURLMarker());
        }
        catch (ClassNotFoundException error)
        {
            /*
             * This is a coding error
             */
            log.error("Bad class in registry", error);
        }
    }


    /**
     * Return a map from service-url-root to the context manager class name.
     *
     * @return list of registered OMIS serviceURLMarkers
     */
    public static List<String> getRegisteredServiceURLMarkers()
    {
        return new ArrayList<>(serviceContextManagerClasses.keySet());
    }


    /**
     * Retrieve a partially filled configuration properties object for an integration service.  It needs the connections for the
     * integration connectors to be added.  Called from admin services.
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
        String                        contextManagerClass = serviceContextManagerClasses.get(serviceURLMarker).getName();

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
            config.setIntegrationServicePartnerOMAS(serviceDescription.getIntegrationServicePartnerOMAS().getAccessServiceFullName());
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


    /**
     * Return descriptions for the list of registered OMISs for this platform.  Returned to external caller.
     *
     * @return list of service descriptions
     */
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


    /**
     * Retrieve the service URL marker for a specific integration connector.
     *
     * @param connectorProviderClassName the name of the connector provider's class
     * @return context manager that matches the capabilities of the connector
     * @throws ConnectionCheckedException the connection passed to the connector provider is not valid
     * @throws ConnectorCheckedException the connector is not valid
     * @throws ClassNotFoundException when the provided class cannot be found
     * @throws InstantiationException when the provided class cannot be instantiated
     * @throws IllegalAccessException when there is insufficient access to instantiate the provided class
     * @throws NoSuchMethodException the default constructor is missing
     * @throws InvocationTargetException unable to call the default constructor
     */
    public static String getIntegrationServiceURLMarker(String connectorProviderClassName) throws ConnectionCheckedException,
                                                                                                  ConnectorCheckedException,
                                                                                                  ClassNotFoundException,
                                                                                                  InstantiationException,
                                                                                                  IllegalAccessException,
                                                                                                  NoSuchMethodException,
                                                                                                  InvocationTargetException
    {
        final String testConnectionQualifiedName = "testConnection";

        ConnectorType connectorType;

        Class<?> connectorProviderClass     = Class.forName(connectorProviderClassName);
        Object   potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

        ConnectorProvider connectorProvider = (ConnectorProvider) potentialConnectorProvider;

        connectorType = connectorProvider.getConnectorType();

        if (connectorType == null)
        {
            connectorType = new ConnectorType();

            connectorType.setConnectorProviderClassName(connectorProviderClassName);
        }

        Connection testConnection = new Connection();

        testConnection.setQualifiedName(testConnectionQualifiedName);
        testConnection.setConnectorType(connectorType);

        Connector connector = connectorProvider.getConnector(testConnection);
        connector.disconnect();

        for (Class<?> apiClass : specializedAPIToServiceMap.keySet())
        {
            if (apiClass.isInstance(connector))
            {
                return specializedAPIToServiceMap.get(apiClass);
            }
        }

        /*
         * The connector is using an interface that is not recognized by the integration daemon.
         */
        return null;
    }
}
