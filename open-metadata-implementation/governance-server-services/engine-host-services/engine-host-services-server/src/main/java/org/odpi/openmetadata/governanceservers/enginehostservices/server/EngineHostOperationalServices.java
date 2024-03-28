/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceContextClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.GovernanceServerEventClient;
import org.odpi.openmetadata.accessservices.governanceserver.client.OpenGovernanceClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworkservices.gaf.client.rest.GAFRESTClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.threads.EngineConfigurationRefreshThread;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * EngineHostOperationalServices is responsible for controlling the startup and shutdown
 * of the engine host services.
 */
public class EngineHostOperationalServices
{
    private final String                         localServerName;               /* Initialized in constructor */
    private final String                         localServerId;                 /* Initialized in constructor */
    private final String                         localServerUserId;             /* Initialized in constructor */
    private final String                         localServerPassword;           /* Initialized in constructor */
    private final int                            maxPageSize;                   /* Initialized in constructor */

    private AuditLog                              auditLog           = null;
    private EngineHostInstance                    engineHostInstance = null;
    private final Map<String, ServerActiveStatus> serviceStatusMap   = new HashMap<>();

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private List<EngineServiceAdmin> engineServiceAdminList = null;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerId unique identifier for this server
     * @param localServerUserId user id for this server to use on REST calls if processing inbound messages.
     * @param localServerPassword user password for this server to use on REST calls if processing inbound messages.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public EngineHostOperationalServices(String localServerName,
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

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Initialize the service.
     *
     * @param configuration config properties
     * @param auditLog destination for audit log messages.
     * @return activated services list
     * @throws OMAGConfigurationErrorException error in configuration preventing startup
     */
    public List<String> initialize(EngineHostServicesConfig configuration,
                                   AuditLog                 auditLog) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "initialize engine host services";
        final String methodName = "initialize";

        this.auditLog = auditLog;

        auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.SERVER_INITIALIZING.getMessageDefinition(localServerName));

        try
        {
            invalidParameterHandler.validateUserId(localServerUserId, methodName);

            /*
             * Handover problem between the admin services and the integration services if the config is null.
             */
            if (configuration == null)
            {
                throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }
            else if (((configuration.getEngineList() == null) || (configuration.getEngineList().isEmpty())) &&
                     ((configuration.getEngineServiceConfigs() == null) || (configuration.getEngineServiceConfigs().isEmpty())))
            {
                throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_ENGINE_SERVICES_CONFIGURED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            /*
             * The configuration for the governance engines is located in an open metadata server. It is accessed through the Governance Engine
             * OMAS.  If the values needed to call the Governance Engine OMAS are not present in the configuration then there is no point in
             * continuing and an exception is thrown.
             */
            String accessServiceRootURL    = this.getAccessServiceRootURL(configuration);
            String accessServiceServerName = this.getAccessServiceServerName(configuration);

            /*
             * Create a REST client to issue REST calls to the Governance Engine OMAS.  This client is then wrapped in the specific API
             * clients.
             */
            GAFRESTClient restClient;
            OpenGovernanceClient openGovernanceClient;
            if (localServerPassword == null)
            {
                restClient = new GAFRESTClient(accessServiceServerName, accessServiceRootURL, auditLog);
                openGovernanceClient = new OpenGovernanceClient(accessServiceServerName, accessServiceRootURL, maxPageSize);
            }
            else
            {
                restClient = new GAFRESTClient(accessServiceServerName, accessServiceRootURL, localServerUserId, localServerUserId, auditLog);
                openGovernanceClient = new OpenGovernanceClient(accessServiceServerName, accessServiceRootURL, localServerUserId, localServerUserId, maxPageSize);
            }

            /*
             * The event client issues a REST call to the Governance Engine OMAS to retrieve the client-side connection to is OutTopic
             * creates the topic connector, listens for incoming and then passes them to any registered listeners.  There are two listeners
             * expected - one used by the engine host services to receive updates to the governance engine configuration and new GovernanceActions
             * - the other used by the Governance Action OMES to receive new Watchdog events for registered Open Watchdog Governance Action Services.
             */
            GovernanceServerEventClient eventClient = new GovernanceServerEventClient(accessServiceServerName,
                                                                                      accessServiceRootURL,
                                                                                      restClient,
                                                                                      maxPageSize,
                                                                                      auditLog,
                                                                                      localServerId);

            /*
             * This is the client used to retrieve configuration and the client used to manage governance action entities
             */
            GovernanceEngineConfigurationClient configurationClient = new GovernanceEngineConfigurationClient(accessServiceServerName,
                                                                                                              accessServiceRootURL,
                                                                                                              restClient,
                                                                                                              maxPageSize,
                                                                                                              auditLog);

            GovernanceContextClient engineActionClient = new GovernanceContextClient(accessServiceServerName,
                                                                                     accessServiceRootURL,
                                                                                     restClient,
                                                                                     maxPageSize);



            /*
             * Initialize each of the integration services and accumulate the integration connector handlers for the
             * integration daemon handler.
             */
            Map<String, List<String>> serviceEngineLists       = new HashMap<>();
            GovernanceEngineMap       governanceEngineHandlers = new GovernanceEngineMap(localServerName,
                                                                                         localServerUserId,
                                                                                         localServerPassword,
                                                                                         configurationClient,
                                                                                         engineActionClient,
                                                                                         auditLog,
                                                                                         maxPageSize);

            /*
             * Add details of the governance engines that have been configured in the engine list.
             */
            if ((configuration.getEngineList() != null) && (! configuration.getEngineList().isEmpty()))
            {
                governanceEngineHandlers.setGovernanceEngineProperties(configuration.getEngineList(),
                                                                       accessServiceServerName,
                                                                       accessServiceRootURL);

            }

            /*
             * All engine services need to be started.  Some may be configured explicitly.  Start with a default list of
             * all engine services using the details of the partner metadata server provided for the engine configuration
             * then overlay the explicitly configured engines.
             */
            Map<String, EngineServiceConfig> engineServiceConfigMap = this.getDefaultEngineServiceConfigs(accessServiceRootURL,
                                                                                                          accessServiceServerName);

            if (configuration.getEngineServiceConfigs() != null)
            {
                for (EngineServiceConfig engineServiceConfig : configuration.getEngineServiceConfigs())
                {
                    if (engineServiceConfig != null)
                    {
                        engineServiceConfigMap.put(engineServiceConfig.getEngineServiceURLMarker(), engineServiceConfig);
                    }
                }
            }


            List<String> activatedServiceList = initializeEngineServices(new ArrayList<>(engineServiceConfigMap.values()),
                                                                         configurationClient,
                                                                         serviceEngineLists,
                                                                         governanceEngineHandlers);

            /*
             * Register a listener for the Governance Engine OMAS out topic.  This call will fail if
             * the metadata server is not running so a separate thread is created to retry the registration request at
             * intervals to wait for the metadata server to restart.  It will also try to retrieve the configuration
             * for the governance engines.
             */
            EngineConfigurationRefreshThread configurationRefreshThread = new EngineConfigurationRefreshThread(governanceEngineHandlers,
                                                                                                               eventClient,
                                                                                                               openGovernanceClient,
                                                                                                               auditLog,
                                                                                                               localServerUserId,
                                                                                                               localServerName,
                                                                                                               accessServiceServerName,
                                                                                                               accessServiceRootURL);
            Thread thread = new Thread(configurationRefreshThread, configurationRefreshThread.getClass().getName());
            thread.start();

            /*
             * Create the engine host instance (it auto-registers in the server map).
             */
            engineHostInstance = new EngineHostInstance(localServerName,
                                                        GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                        auditLog,
                                                        localServerUserId,
                                                        maxPageSize,
                                                        configurationRefreshThread,
                                                        governanceEngineHandlers);



            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.SERVER_INITIALIZED.getMessageDefinition(localServerName));

            return activatedServiceList;
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
                                  EngineHostServicesAuditCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName,
                                                                                                            error.getClass().getName(),
                                                                                                            error.getMessage()),
                                  error.toString(),
                                  error);

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.SERVICE_INSTANCE_FAILURE.getMessageDefinition(localServerName,
                                                                                                                                error.getClass().getName(),
                                                                                                                                error.getMessage()),
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
     * Use the partner access service information to create a default definition for each engine service.
     *
     * @param partnerURLRoot platform URL root for the partner access service set up for the engine host
     * @param partnerServerName name of the server where the access service is located
     * @return map of default engine service configurations
     */
    private Map<String, EngineServiceConfig> getDefaultEngineServiceConfigs(String partnerURLRoot,
                                                                            String partnerServerName)
    {
        Map<String, EngineServiceConfig> engineServiceConfigList = new HashMap<>();

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            EngineServiceRegistrationEntry engineServiceRegistrationEntry = OMAGEngineServiceRegistration.getEngineServiceRegistration(engineServiceDescription.getEngineServiceURLMarker());

            if (engineServiceRegistrationEntry != null)
            {
                EngineServiceConfig engineServiceConfig = new EngineServiceConfig(engineServiceRegistrationEntry);

                engineServiceConfig.setOMAGServerPlatformRootURL(partnerURLRoot);
                engineServiceConfig.setOMAGServerName(partnerServerName);

                engineServiceConfigList.put(engineServiceRegistrationEntry.getEngineServiceURLMarker(), engineServiceConfig);
            }
        }

        return engineServiceConfigList;
    }


    /**
     * Start up the engine services.
     *
     * @param engineServiceConfigList       configured engine services
     * @param configurationClient           client needed to retrieve governance engine definitions
     * @return activatedServiceList          list of engine services running in the server
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private List<String> initializeEngineServices(List<EngineServiceConfig>            engineServiceConfigList,
                                                  GovernanceEngineConfigurationClient  configurationClient,
                                                  Map<String, List<String>>            serviceEngineLists,
                                                  GovernanceEngineMap                  governanceEngineHandlers) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeEngineServices";
        final String actionDescription = "Initialize Engine Services";

        engineServiceAdminList = new ArrayList<>();

        /*
         * Process the list explicitly configured engine services.
         */
        if (engineServiceConfigList != null)
        {
            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.STARTING_ENGINE_SERVICES.getMessageDefinition(localServerName));

            /*
             * Need to count the engine services because of the possibility of deprecated or disabled engine services in the list.
             */
            int configuredEngineServiceCount = 0;
            int enabledEngineServiceCount = 0;

            for (EngineServiceConfig engineServiceConfig : engineServiceConfigList)
            {
                configuredEngineServiceCount++;

                if (ServiceOperationalStatus.ENABLED.equals(engineServiceConfig.getEngineServiceOperationalStatus()))
                {
                    enabledEngineServiceCount++;
                    this.setServerServiceActiveStatus(engineServiceConfig.getEngineServiceFullName(), ServerActiveStatus.STARTING);

                    serviceEngineLists.put(engineServiceConfig.getEngineServiceURLMarker(),
                                           governanceEngineHandlers.getGovernanceEngineNames(engineServiceConfig.getEngineServiceURLMarker()));

                    try
                    {
                        EngineServiceAdmin engineServiceAdmin = this.getEngineServiceAdminClass(engineServiceConfig);

                        /*
                         * Each engine service has its own audit log instance.
                         */
                        AuditLog engineServicesAuditLog
                                = auditLog.createNewAuditLog(engineServiceConfig.getEngineServiceId(),
                                                             engineServiceConfig.getEngineServiceDevelopmentStatus(),
                                                             engineServiceConfig.getEngineServiceFullName(),
                                                             engineServiceConfig.getEngineServiceDescription(),
                                                             engineServiceConfig.getEngineServiceWiki());

                         engineServiceAdmin.initialize(localServerId,
                                                       localServerName,
                                                       engineServicesAuditLog,
                                                       localServerUserId,
                                                       localServerPassword,
                                                       maxPageSize,
                                                       configurationClient,
                                                       engineServiceConfig,
                                                       governanceEngineHandlers);

                        engineServiceAdminList.add(engineServiceAdmin);
                        this.setServerServiceActiveStatus(engineServiceConfig.getEngineServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                    catch (OMAGConfigurationErrorException error)
                    {
                        auditLog.logException(methodName,
                                              EngineHostServicesAuditCode.ENGINE_SERVICE_INSTANCE_FAILURE.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                              engineServiceConfig.toString(),
                                              error);
                        throw error;
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              EngineHostServicesAuditCode.ENGINE_SERVICE_INSTANCE_FAILURE.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                              engineServiceConfig.toString(),
                                              error);

                        throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(localServerName,
                                                                                                                                                       engineServiceConfig.getEngineServiceName(),
                                                                                                                                                       error.getClass().getName(),
                                                                                                                                                       error.getMessage()),
                                                                  this.getClass().getName(),
                                                                  methodName,
                                                                  error);
                    }
                }
                else
                {
                    auditLog.logMessage(actionDescription,
                                        EngineHostServicesAuditCode.SKIPPING_ENGINE_SERVICE.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                                 localServerName));
                }

            }

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.ALL_ENGINE_SERVICES_STARTED.getMessageDefinition(Integer.toString(enabledEngineServiceCount),
                                                                                                             Integer.toString(configuredEngineServiceCount),
                                                                                                             localServerName));
        }

        /*
         * Save the list of running engine services to the instance and then add the instance to the instance map.
         * The instance information can then be retrieved for shutdown or other management requests.
         */
        return new ArrayList<>(serviceStatusMap.keySet());
    }


    /**
     * Shutdown the service.
     */
    public void terminate()
    {
        final String actionDescription = "terminate";

        auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.SERVER_SHUTTING_DOWN.getMessageDefinition(localServerName));

        if (engineHostInstance != null)
        {
            engineHostInstance.shutdown();
        }

        /*
         * Shutdown the engine services
         */
        if (engineServiceAdminList != null)
        {
            for (EngineServiceAdmin engineServiceAdmin : engineServiceAdminList)
            {
                if (engineServiceAdmin != null)
                {
                    engineServiceAdmin.shutdown();
                }
            }
        }


        auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }



    /**
     * Create an instance of the engine service's admin class from the class name in the configuration.
     *
     * @param engineServiceConfig configuration for the engine service
     * @return Admin class for the engine service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private EngineServiceAdmin getEngineServiceAdminClass(EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String methodName = "getEngineServiceAdminClass";

        String    engineServiceAdminClassName = engineServiceConfig.getEngineServiceAdminClass();

        if (engineServiceAdminClassName != null)
        {
            try
            {
                return (EngineServiceAdmin) Class.forName(engineServiceAdminClassName).getDeclaredConstructor().newInstance();
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException| NoSuchMethodException | InvocationTargetException error)
            {
                auditLog.logException(methodName,
                                      EngineHostServicesAuditCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                      engineServiceAdminClassName,
                                                                                                                      error.getMessage()),
                                      engineServiceConfig.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                                          engineServiceAdminClassName,
                                                                                                                                          engineServiceConfig.getEngineServiceName()),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      EngineHostServicesAuditCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                      engineServiceAdminClassName,
                                                                                                                      error.getMessage()),
                                      engineServiceConfig.toString(),
                                      error);

                throw error;
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                EngineHostServicesAuditCode.NULL_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                 engineServiceConfig.getEngineServiceFullName()),
                                engineServiceConfig.toString());

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NULL_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                                       engineServiceConfig.getEngineServiceName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }



    /**
     * Return the open metadata server's root URL from the configuration.
     *
     * @param config configuration
     * @return root URL
     * @throws OMAGConfigurationErrorException No root URL present in the config
     */
    private String getAccessServiceRootURL(EngineHostServicesConfig config) throws OMAGConfigurationErrorException
    {
        String accessServiceRootURL = config.getOMAGServerPlatformRootURL();

        if (accessServiceRootURL == null)
        {
            final String actionDescription = "Validate engine services configuration.";
            final String methodName        = "getPartnerServiceRootURL";

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.NO_CONFIG_OMAS_SERVER_URL.getMessageDefinition(localServerName,
                                                                                                    AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName()));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_CONFIG_OMAS_SERVER_URL.getMessageDefinition(localServerName,
                                                                                                                          AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceRootURL;
    }


    /**
     * Return the open metadata server's name from the configuration.
     *
     * @param config configuration
     * @return server name
     * @throws OMAGConfigurationErrorException No server name present in the config
     */
    private String getAccessServiceServerName(EngineHostServicesConfig config) throws OMAGConfigurationErrorException
    {
        String accessServiceServerName = config.getOMAGServerName();

        if (accessServiceServerName == null)
        {
            final String actionDescription = "Validate engine service configuration.";
            final String methodName        = "getPartnerServiceServerName";

            auditLog.logMessage(actionDescription,
                                EngineHostServicesAuditCode.NO_CONFIG_OMAS_SERVER_NAME.getMessageDefinition(localServerName,
                                                                                                            AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceName()));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_CONFIG_OMAS_SERVER_NAME.getMessageDefinition(localServerName,
                                                                                                                                  AccessServiceDescription.GOVERNANCE_ENGINE_OMAS.getAccessServiceFullName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceServerName;
    }
}
