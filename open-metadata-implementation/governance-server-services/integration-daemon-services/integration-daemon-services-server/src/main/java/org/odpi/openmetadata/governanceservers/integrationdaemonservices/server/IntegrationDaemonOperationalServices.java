/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceServerEventClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.IntegrationGroupConfigurationClient;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.frameworkservices.oif.client.rest.OpenIntegrationRESTClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorCacheMap;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceRegistry;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.GroupConfigurationRefreshThread;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * IntegrationDaemonOperationalServices is responsible for controlling the startup and shutdown of
 * the integration daemon services.
 */
public class IntegrationDaemonOperationalServices
{
    private final String                         localServerName;               /* Initialized in constructor */
    private final String                         localServerId;                 /* Initialized in constructor */
    private final String                         localServerUserId;             /* Initialized in constructor */
    private final String                         localServerPassword;           /* Initialized in constructor */
    private final int                            maxPageSize;                   /* Initialized in constructor */


    private AuditLog                        auditLog                  = null;
    private IntegrationDaemonInstance       integrationDaemonInstance = null;
    private final Map<String, ServerActiveStatus> serviceStatusMap          = new HashMap<>();

    private final List<GroupConfigurationRefreshThread> configurationRefreshThreads = new ArrayList<>();

    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerId unique identifier for this server
     * @param localServerUserId user id for this server to use on REST calls if processing inbound messages.
     * @param localServerPassword user password for this server to use on REST calls if processing inbound messages.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public IntegrationDaemonOperationalServices(String localServerName,
                                                String localServerId,
                                                String localServerUserId,
                                                String localServerPassword,
                                                int    maxPageSize)
    {
        this.localServerName       = localServerName;
        this.localServerId         = localServerId;
        this.localServerUserId     = localServerUserId;
        this.localServerPassword   = localServerPassword;
        this.maxPageSize           = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param staticConfiguration config properties for static integration services
     * @param dynamicConfiguration config properties for dynamic integration groups
     * @param auditLog destination for audit log messages.
     *
     * @return activated services list
     * @throws OMAGConfigurationErrorException error in configuration preventing startup
     */
    public List<String> initialize(List<IntegrationServiceConfig> staticConfiguration,
                                   List<IntegrationGroupConfig>   dynamicConfiguration,
                                   AuditLog                       auditLog) throws OMAGConfigurationErrorException
    {
        final String             actionDescription = "initialize";
        final String             methodName = "initialize";

        this.auditLog = auditLog;

        auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            /*
             * Handover problem between the admin services and the integration services if the config is null.
             */
            if (((staticConfiguration == null) || (staticConfiguration.isEmpty())) &&
                        ((dynamicConfiguration == null) || (dynamicConfiguration.isEmpty())))
            {
                throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            IntegrationConnectorCacheMap           daemonConnectorHandlers      = new IntegrationConnectorCacheMap();
            Map<String, IntegrationServiceHandler> integrationServiceHandlerMap = new HashMap<>();

            if ((staticConfiguration == null) || (staticConfiguration.isEmpty()))
            {
                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.NO_INTEGRATION_SERVICES_CONFIGURED.getMessageDefinition(localServerName));
            }
            else
            {
                /*
                 * Initialize each of the integration services and accumulate the integration connector handlers for the
                 * integration daemon handler.
                 */
                for (IntegrationServiceConfig integrationServiceConfig : staticConfiguration)
                {
                    if (integrationServiceConfig != null)
                    {
                        this.setServerServiceActiveStatus(integrationServiceConfig.getIntegrationServiceFullName(), ServerActiveStatus.STARTING);

                        String                    partnerOMASRootURL          = this.getPartnerOMASRootURL(integrationServiceConfig);
                        String                    partnerOMASServerName       = this.getPartnerOMASServerName(integrationServiceConfig);
                        String                    integrationServiceURLMarker = this.getServiceURLMarker(integrationServiceConfig);
                        IntegrationContextManager contextManager              = this.getContextManager(integrationServiceConfig);

                        if (integrationServiceConfig.getDefaultPermittedSynchronization() == null)
                        {
                            auditLog.logMessage(actionDescription,
                                                IntegrationDaemonServicesAuditCode.NO_PERMITTED_SYNCHRONIZATION.getMessageDefinition(localServerName),
                                                integrationServiceConfig.toString());

                            throw new OMAGConfigurationErrorException(
                                    IntegrationDaemonServicesErrorCode.NO_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                                            integrationServiceConfig.getIntegrationServiceFullName(),
                                            localServerName),
                                    this.getClass().getName(),
                                    methodName);
                        }

                        /*
                         * Each integration service has its own audit log instance.
                         */
                        AuditLog integrationServicesAuditLog
                                = auditLog.createNewAuditLog(integrationServiceConfig.getIntegrationServiceId(),
                                                             integrationServiceConfig.getIntegrationServiceDevelopmentStatus(),
                                                             integrationServiceConfig.getIntegrationServiceFullName(),
                                                             integrationServiceConfig.getIntegrationServiceDescription(),
                                                             integrationServiceConfig.getIntegrationServiceWiki());
                        contextManager.initializeContextManager(partnerOMASServerName,
                                                                partnerOMASRootURL,
                                                                localServerUserId,
                                                                localServerPassword,
                                                                integrationServiceConfig.getIntegrationServiceOptions(),
                                                                maxPageSize,
                                                                integrationServicesAuditLog);

                        contextManager.createClients(maxPageSize);

                        IntegrationServiceHandler integrationServiceHandler = new IntegrationServiceHandler(localServerName,
                                                                                                            localServerUserId,
                                                                                                            integrationServiceConfig,
                                                                                                            contextManager,
                                                                                                            auditLog);

                        List<IntegrationConnectorHandler> serviceConnectorHandlers = integrationServiceHandler.initialize();
                        if (serviceConnectorHandlers != null)
                        {
                            for (IntegrationConnectorHandler connectorHandler : serviceConnectorHandlers)
                            {
                                daemonConnectorHandlers.putHandlerByConnectorId(connectorHandler.getIntegrationConnectorId(),
                                                                                connectorHandler,
                                                                                true);
                            }
                        }

                        integrationServiceHandlerMap.put(integrationServiceURLMarker, integrationServiceHandler);
                        this.setServerServiceActiveStatus(integrationServiceConfig.getIntegrationServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                }
            }

            Map<String, IntegrationGroupHandler> integrationGroupHandlerMap = new HashMap<>();

            if ((dynamicConfiguration == null) || (dynamicConfiguration.isEmpty()))
            {
                auditLog.logMessage(actionDescription,
                                    IntegrationDaemonServicesAuditCode.NO_INTEGRATION_GROUPS_CONFIGURED.getMessageDefinition(localServerName));
            }
            else
            {
                List<String> registeredServiceURLs = IntegrationServiceRegistry.getRegisteredServiceURLMarkers();

                for (IntegrationGroupConfig integrationGroupConfig : dynamicConfiguration)
                {
                    if (integrationGroupConfig != null)
                    {
                        String partnerOMASRootURL    = this.getPartnerOMASRootURL(integrationGroupConfig);
                        String partnerOMASServerName = this.getPartnerOMASServerName(integrationGroupConfig);
                        String groupName             = this.getIntegrationGroupName(integrationGroupConfig);

                        GAFRESTClient               gafRESTClient = new GAFRESTClient(partnerOMASServerName, partnerOMASRootURL, auditLog);
                        OpenIntegrationRESTClient   openIntegrationRESTClient = new OpenIntegrationRESTClient(partnerOMASServerName, partnerOMASRootURL, auditLog);
                        GovernanceServerEventClient eventClient = new GovernanceServerEventClient(partnerOMASServerName,
                                                                                                  partnerOMASRootURL,
                                                                                                  gafRESTClient,
                                                                                                  maxPageSize,
                                                                                                  auditLog,
                                                                                                  localServerId+groupName);
                        IntegrationGroupConfigurationClient configurationClient = new IntegrationGroupConfigurationClient(partnerOMASServerName,
                                                                                                                          partnerOMASRootURL,
                                                                                                                          openIntegrationRESTClient,
                                                                                                                          maxPageSize,
                                                                                                                          auditLog);

                        Map<String, IntegrationContextManager> contextManagerMap = new HashMap<>();
                        Map<String, String>                    integrationServiceNameMap = new HashMap<>();

                        for (String registeredServiceURLMarker : registeredServiceURLs)
                        {
                            IntegrationServiceConfig integrationServiceConfig = IntegrationServiceRegistry.getIntegrationServiceConfig(registeredServiceURLMarker,
                                                                                                                                       localServerName,
                                                                                                                                       methodName);
                            IntegrationContextManager serviceContextManager = this.getContextManager(integrationServiceConfig);

                            /*
                             * Each integration service has its own audit log instance.
                             */
                            AuditLog integrationServicesAuditLog
                                    = auditLog.createNewAuditLog(integrationServiceConfig.getIntegrationServiceId(),
                                                                 integrationServiceConfig.getIntegrationServiceDevelopmentStatus(),
                                                                 integrationServiceConfig.getIntegrationServiceFullName(),
                                                                 integrationServiceConfig.getIntegrationServiceDescription(),
                                                                 integrationServiceConfig.getIntegrationServiceWiki());
                            serviceContextManager.initializeContextManager(partnerOMASServerName,
                                                                           partnerOMASRootURL,
                                                                           localServerUserId,
                                                                           localServerPassword,
                                                                           integrationServiceConfig.getIntegrationServiceOptions(),
                                                                           maxPageSize,
                                                                           integrationServicesAuditLog);

                            serviceContextManager.createClients(maxPageSize);


                            contextManagerMap.put(registeredServiceURLMarker, serviceContextManager);
                            integrationServiceNameMap.put(registeredServiceURLMarker, integrationServiceConfig.getIntegrationServiceFullName());
                        }

                        IntegrationGroupHandler groupHandler = new IntegrationGroupHandler(integrationGroupConfig.getIntegrationGroupQualifiedName(),
                                                                                           contextManagerMap,
                                                                                           integrationServiceNameMap,
                                                                                           daemonConnectorHandlers,
                                                                                           localServerName,
                                                                                           localServerUserId,
                                                                                           configurationClient,
                                                                                           auditLog,
                                                                                           maxPageSize);

                        integrationGroupHandlerMap.put(groupName, groupHandler);

                        /*
                         * Register a listener for the Governance Engine OMAS out topic.  This call will fail if
                         * the metadata server is not running so a separate thread is created to retry the registration request at
                         * intervals to wait for the metadata server to restart.  It will also try to retrieve the configuration
                         * for the governance engines.
                         */
                        GroupConfigurationRefreshThread configurationRefreshThread = new GroupConfigurationRefreshThread(groupName,
                                                                                                                         groupHandler,
                                                                                                                         eventClient,
                                                                                                                         auditLog,
                                                                                                                         localServerUserId,
                                                                                                                         localServerName,
                                                                                                                         partnerOMASServerName,
                                                                                                                         partnerOMASRootURL);
                        configurationRefreshThreads.add(configurationRefreshThread);
                        Thread thread = new Thread(configurationRefreshThread, configurationRefreshThread.getClass().getName());
                        thread.start();
                    }
                }
            }

            /*
             * Create the integration daemon instance.
             */
            integrationDaemonInstance = new IntegrationDaemonInstance(localServerName,
                                                                      GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(),
                                                                      auditLog,
                                                                      localServerUserId,
                                                                      maxPageSize,
                                                                      integrationServiceHandlerMap,
                                                                      integrationGroupHandlerMap,
                                                                      daemonConnectorHandlers);

            auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_INITIALIZED.getMessageDefinition(localServerName));

            return new ArrayList<>(serviceStatusMap.keySet());
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logException(actionDescription,
                                  IntegrationDaemonServicesAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName, error.getMessage()),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      error);
        }
    }


    /**
     * Set the status of a particular service.
     *
     * @param serviceName name of service
     * @param activeStatus new status
     */
    private synchronized void setServerServiceActiveStatus(String serviceName, ServerActiveStatus activeStatus)
    {
        serviceStatusMap.put(serviceName, activeStatus);
    }


    /**
     * Return a summary of the status of this server and the services within it.
     *
     * @return server status
     */
    public List<OMAGServerServiceStatus> getServiceStatuses()
    {
        List<OMAGServerServiceStatus> serviceStatuses = new ArrayList<>();

        for (String serviceName : serviceStatusMap.keySet())
        {
            OMAGServerServiceStatus serviceStatus = new OMAGServerServiceStatus();

            serviceStatus.setServiceName(serviceName);
            serviceStatus.setServiceStatus(serviceStatusMap.get(serviceName));

            serviceStatuses.add(serviceStatus);
        }

        return serviceStatuses;
    }


    /**
     * Return an integration group's name from the configuration.
     *
     * @param integrationGroupConfig configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    private String getIntegrationGroupName(IntegrationGroupConfig integrationGroupConfig) throws OMAGConfigurationErrorException
    {
        String integrationGroupName = integrationGroupConfig.getIntegrationGroupQualifiedName();

        if (integrationGroupName == null)
        {
            final String actionDescription = "Validate integration group configuration.";
            final String methodName        = "getIntegrationGroupName";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName));

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return integrationGroupName;
    }


    /**
     * Return the open metadata server's root URL from the configuration.
     *
     * @param integrationServicesConfig configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    private String getPartnerOMASRootURL(IntegrationServiceConfig integrationServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = integrationServicesConfig.getOMAGServerPlatformRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "Validate integration service configuration.";
            final String methodName        = "getPartnerOMASRootURL";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                                integrationServicesConfig.toString());

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceRootURL;
    }


    /**
     * Return the open metadata server's root URL from the configuration.
     *
     * @param integrationGroupConfig configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    private String getPartnerOMASRootURL(IntegrationGroupConfig integrationGroupConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = integrationGroupConfig.getOMAGServerPlatformRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "Validate integration group configuration.";
            final String methodName        = "getPartnerOMASRootURL";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                                integrationGroupConfig.toString());

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceRootURL;
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param integrationServicesConfig configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    private String getPartnerOMASServerName(IntegrationServiceConfig integrationServicesConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = integrationServicesConfig.getOMAGServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "Validate integration service configuration.";
            final String methodName        = "getPartnerOMASServerName";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                                integrationServicesConfig.toString());

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceServerName;
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param integrationGroupConfig configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    private String getPartnerOMASServerName(IntegrationGroupConfig integrationGroupConfig) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = integrationGroupConfig.getOMAGServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "Validate integration group configuration.";
            final String methodName        = "getPartnerOMASServerName";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                                integrationGroupConfig.toString());

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceServerName;
    }


    /**
     * Return an instance of the context manager class.  This is needed by the integration service handler.
     *
     * @param integrationServiceConfig configuration for the integration service
     * @return context manager class for the integration service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private IntegrationContextManager getContextManager(IntegrationServiceConfig integrationServiceConfig) throws OMAGConfigurationErrorException
    {
        final String methodName = "getContextManager";

        String contextManagerClassName = integrationServiceConfig.getIntegrationServiceContextManagerClass();

        if (contextManagerClassName != null)
        {
            try
            {
                return (IntegrationContextManager) Class.forName(contextManagerClassName).getDeclaredConstructor().newInstance();
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException error)
            {
                auditLog.logException(methodName,
                                      IntegrationDaemonServicesAuditCode.INVALID_CONTEXT_MANAGER.
                                              getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName(),
                                                                   contextManagerClassName,
                                                                   error.getClass().getName(),
                                                                   error.getMessage()),
                                      integrationServiceConfig.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.INVALID_CONTEXT_MANAGER.
                                                                  getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName(),
                                                                                       contextManagerClassName,
                                                                                       error.getClass().getName(),
                                                                                       error.getMessage()),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      IntegrationDaemonServicesAuditCode.INVALID_CONTEXT_MANAGER.
                                              getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName(),
                                                                   contextManagerClassName,
                                                                   error.getClass().getName(),
                                                                   error.getMessage()),
                                      integrationServiceConfig.toString(),
                                      error);

                throw error;
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                IntegrationDaemonServicesAuditCode.NULL_CONTEXT_MANAGER.
                                        getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName(),
                                                             localServerName),
                                integrationServiceConfig.toString());

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_CONTEXT_MANAGER.
                                                              getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Validate that details about the integration service are specified.
     *
     * @param integrationServiceConfig configuration for the integration service
     * @return the integration service's URL marker - use on REST calls
     * @throws OMAGConfigurationErrorException there is a missing value in the configuration
     */
    private String getServiceURLMarker(IntegrationServiceConfig integrationServiceConfig) throws OMAGConfigurationErrorException
    {
        final String methodName = "getServiceURLMarker";

        final String serviceNamePropertyName      = "integrationServiceName";
        final String serviceFullNamePropertyName  = "integrationServiceFullName";
        final String serviceURLMarkerPropertyName = "integrationServiceURLMarker";
        final String unknownValue                 = "???";

        if ((integrationServiceConfig.getIntegrationServiceName() == null) || (integrationServiceConfig.getIntegrationServiceName().isEmpty()))
        {
            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(serviceNamePropertyName,
                                                                                                                                        unknownValue,
                                                                                                                                        localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if ((integrationServiceConfig.getIntegrationServiceFullName() == null) || (integrationServiceConfig.getIntegrationServiceFullName().isEmpty()))
        {
            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(serviceFullNamePropertyName,
                                                                                                                                        integrationServiceConfig.getIntegrationServiceName(),
                                                                                                                                        localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if ((integrationServiceConfig.getIntegrationServiceURLMarker() == null) || (integrationServiceConfig.getIntegrationServiceURLMarker().isEmpty()))
        {
            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(serviceURLMarkerPropertyName,
                                                                                                                                        integrationServiceConfig.getIntegrationServiceFullName(),
                                                                                                                                        localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return integrationServiceConfig.getIntegrationServiceURLMarker();
    }


    /**
     * Shutdown the service.
     */
    public void terminate()
    {
        final String actionDescription = "terminate";

        auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        for (String serviceName : serviceStatusMap.keySet())
        {
            serviceStatusMap.put(serviceName, ServerActiveStatus.STOPPING);
        }

        for (GroupConfigurationRefreshThread groupConfigurationRefreshThread : configurationRefreshThreads)
        {
            groupConfigurationRefreshThread.stop();
        }

        if (integrationDaemonInstance != null)
        {
            integrationDaemonInstance.shutdown();
        }

        for (String serviceName : serviceStatusMap.keySet())
        {
            serviceStatusMap.put(serviceName, ServerActiveStatus.INACTIVE);
        }

        auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
