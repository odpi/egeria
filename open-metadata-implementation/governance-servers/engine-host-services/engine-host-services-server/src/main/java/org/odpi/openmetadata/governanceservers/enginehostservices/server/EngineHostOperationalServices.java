/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineConfigurationClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.GovernanceEngineEventClient;
import org.odpi.openmetadata.accessservices.governanceengine.client.rest.GovernanceEngineRESTClient;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineHostServicesConfig;
import org.odpi.openmetadata.adminservices.configuration.properties.EngineServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.adminservices.properties.ServerActiveStatus;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.GovernanceEngineHandler;
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

    private AuditLog                        auditLog           = null;
    private EngineHostInstance              engineHostInstance = null;
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
            else if ((configuration.getEngineServiceConfigs() == null) || configuration.getEngineServiceConfigs().isEmpty())
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
            GovernanceEngineRESTClient restClient;
            if (localServerPassword == null)
            {
                restClient = new GovernanceEngineRESTClient(accessServiceServerName, accessServiceRootURL, auditLog);
            }
            else
            {
                restClient = new GovernanceEngineRESTClient(accessServiceServerName, accessServiceRootURL, localServerUserId, localServerUserId, auditLog);
            }

            /*
             * The event client issues a REST call to the Governance Engine OMAS to retrieve the client-side connection to is OutTopic
             * creates the topic connector, listens for incoming and then passes them to any registered listeners.  There are two listeners
             * expected - one used by the engine host services to receive updates to the governance engine configuration and new GovernanceActions
             * - the other used by the Governance Action OMES to receive new Watchdog events for registered Open Watchdog Governance Action Services.
             */
            GovernanceEngineEventClient eventClient = new GovernanceEngineEventClient(accessServiceServerName,
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

            GovernanceEngineClient serverClient = new GovernanceEngineClient(accessServiceServerName,
                                                                             accessServiceRootURL,
                                                                             restClient,
                                                                             maxPageSize);

            /*
             * Initialize each of the integration services and accumulate the integration connector handlers for the
             * integration daemon handler.
             */
            Map<String, List<String>>            serviceEngineLists       = new HashMap<>();
            Map<String, GovernanceEngineHandler> governanceEngineHandlers = new HashMap<>();

            List<String> activatedServiceList = initializeEngineServices(configuration.getEngineServiceConfigs(),
                                                                         configurationClient,
                                                                         serverClient,
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
                                                        governanceEngineHandlers,
                                                        serviceEngineLists);

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
     * Start up the engine services.
     *
     * @param engineServiceConfigList       configured engine services
     * @param configurationClient           client needed to retrieve governance engine definitions
     * @param serverClient                  client needed to manage governance actions
     * @return activatedServiceList          list of engine services running in the server
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private List<String> initializeEngineServices(List<EngineServiceConfig>            engineServiceConfigList,
                                                  GovernanceEngineConfigurationClient  configurationClient,
                                                  GovernanceEngineClient               serverClient,
                                                  Map<String, List<String>>            serviceEngineLists,
                                                  Map<String, GovernanceEngineHandler> governanceEngineHandlers) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeEngineServices";
        final String actionDescription = "Initialize Engine Services";

        engineServiceAdminList = new ArrayList<>();

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

                    serviceEngineLists.put(engineServiceConfig.getEngineServiceURLMarker(), this.getEngineNames(engineServiceConfig));

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

                        Map<String, GovernanceEngineHandler> serviceEngineHandlers = engineServiceAdmin.initialize(localServerId,
                                                                                                                   localServerName,
                                                                                                                   engineServicesAuditLog,
                                                                                                                   localServerUserId,
                                                                                                                   localServerPassword,
                                                                                                                   maxPageSize,
                                                                                                                   configurationClient,
                                                                                                                   serverClient,
                                                                                                                   engineServiceConfig);

                        if ((serviceEngineHandlers == null) || (serviceEngineHandlers.isEmpty()))
                        {
                            auditLog.logMessage(methodName,
                                                  EngineHostServicesAuditCode.ENGINE_SERVICE_NULL_HANDLERS.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                                localServerName),
                                                  engineServiceConfig.toString());

                            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.ENGINE_SERVICE_NULL_HANDLERS.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                                                    localServerName),
                                                                      this.getClass().getName(),
                                                                      methodName);
                        }

                        governanceEngineHandlers.putAll(serviceEngineHandlers);
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

        engineHostInstance.shutdown();

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


    /**
     * Retrieve the list of engine names for this engine service from the configuration.
     *
     * @param engineServiceConfig configuration
     * @return list of engines
     * @throws OMAGConfigurationErrorException an issue in the configuration prevented initialization
     */
    private List<String> getEngineNames(EngineServiceConfig engineServiceConfig) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "Validate engine services configuration.";
        final String methodName        = "getEngineNames";

        List<EngineConfig> engineConfigList = engineServiceConfig.getEngines();

        if ((engineConfigList == null) || (engineConfigList.isEmpty()))
        {
            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.NO_ENGINES_FOR_SERVICE.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                      localServerName));

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_ENGINES_FOR_SERVICE.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                         localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
        else
        {
            List<String>  engineNames = new ArrayList<>();

            for (EngineConfig engineConfig : engineConfigList)
            {
                if (engineConfig != null)
                {

                    if (engineConfig.getEngineQualifiedName() == null)
                    {
                        auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.NULL_ENGINE_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                                  localServerName));

                        throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NULL_ENGINE_NAME.getMessageDefinition(engineServiceConfig.getEngineServiceFullName(),
                                                                                                                                          localServerName),
                                                                  this.getClass().getName(),
                                                                  methodName);
                    }

                    engineNames.add(engineConfig.getEngineQualifiedName());
                }
            }


            if (engineNames.isEmpty())
            {
                return null;
            }

            return engineNames;
        }
    }
}
