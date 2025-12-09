/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.enginehostservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.EngineConfig;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.EngineServiceDefinition;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataEventClient;
import org.odpi.openmetadata.governanceservers.enginehostservices.admin.EngineServiceAdmin;
import org.odpi.openmetadata.governanceservers.enginehostservices.enginemap.GovernanceEngineMap;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesAuditCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.ffdc.EngineHostServicesErrorCode;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.governanceservers.enginehostservices.threads.EngineConfigurationRefreshThread;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

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
    private final String localServerName;                 /* Initialized in constructor */
    private final String localServerUserId;                /* Initialized in constructor */
    private final String localServerSecretStoreProvider;   /* Initialized in constructor */
    private final String localServerSecretStoreLocation;   /* Initialized in constructor */
    private final String localServerSecretStoreCollection; /* Initialized in constructor */
    private final int    maxPageSize;                      /* Initialized in constructor */

    private AuditLog                              auditLog           = null;
    private EngineHostInstance                    engineHostInstance = null;
    private final Map<String, ServerActiveStatus> serviceStatusMap   = new HashMap<>();

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private List<EngineServiceAdmin> engineServiceAdminList = null;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerUserId user id for this server to use on metadata requests.
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public EngineHostOperationalServices(String localServerName,
                                         String localServerUserId,
                                         String localServerSecretsStoreProvider,
                                         String localServerSecretsStoreLocation,
                                         String localServerSecretsStoreCollection,
                                         int    maxPageSize)
    {
        this.localServerName                  = localServerName;
        this.localServerUserId                = localServerUserId;
        this.localServerSecretStoreProvider   = localServerSecretsStoreProvider;
        this.localServerSecretStoreLocation   = localServerSecretsStoreLocation;
        this.localServerSecretStoreCollection = localServerSecretsStoreCollection;
        this.maxPageSize                      = maxPageSize;

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
    public List<String> initialize(List<EngineConfig> configuration,
                                   AuditLog           auditLog) throws OMAGConfigurationErrorException
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
            else if (configuration.isEmpty())
            {
                throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NO_ENGINE_SERVICES_CONFIGURED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            /*
             * All engine services need to be started. The simply provide support for the REST API.
             */
            Map<String, EngineServiceDefinition> engineServiceConfigMap = this.getDefaultEngineServiceConfigs();

            List<String> activatedServiceList = initializeEngineServices(new ArrayList<>(engineServiceConfigMap.values()));

            /*
             * Create the synchronized map that manages the active engines.
             */
            GovernanceEngineMap governanceEngineHandlers = new GovernanceEngineMap(localServerName,
                                                                                   localServerUserId,
                                                                                   auditLog,
                                                                                   maxPageSize);

            /*
             * Add details of the governance engines that have been configured in the engine list.
             */
            for (EngineConfig governanceEngine : configuration)
            {
                /*
                 * The event client issues a REST call to the Open Metadata Services to retrieve the client-side connection to is OutTopic
                 * creates the topic connector, listens for incoming and then passes them to any registered listeners.  There are two listeners
                 * expected - one used by the engine host services to receive updates to the governance engine configuration and new GovernanceActions
                 * - the other used by the Governance Action OMES to receive new Watchdog events for registered Open Watchdog Governance Action Services.
                 */
                OpenMetadataEventClient eventClient = new EgeriaOpenMetadataEventClient(governanceEngine.getOMAGServerName(),
                                                                                        governanceEngine.getOMAGServerPlatformRootURL(),
                                                                                        governanceEngine.getEngineUserId(),
                                                                                        governanceEngine.getSecretsStoreProvider(),
                                                                                        governanceEngine.getSecretsStoreLocation(),
                                                                                        governanceEngine.getSecretsStoreCollection(),
                                                                                        maxPageSize,
                                                                                        auditLog,
                                                                                        governanceEngine.getEngineId());

                /*
                 * This thread is responsible for managing the retrieval of the engine definitions and registering the listener for events.
                 */
                EngineConfigurationRefreshThread configurationRefreshThread = new EngineConfigurationRefreshThread(governanceEngine,
                                                                                                                   governanceEngineHandlers,
                                                                                                                   eventClient,
                                                                                                                   auditLog,
                                                                                                                   localServerName);

                /*
                 * Create an entry in the governance engine map for the configured engine.
                 */
                governanceEngineHandlers.setGovernanceEngineProperties(governanceEngine,
                                                                       eventClient,
                                                                       configurationRefreshThread);

                /*
                 * This engine is ready to go - start its management thread
                 */
                Thread thread = new Thread(configurationRefreshThread, configurationRefreshThread.getClass().getName());
                thread.start();
            }

            /*
             * Create the engine host instance (it auto-registers in the server map).
             */
            engineHostInstance = new EngineHostInstance(localServerName,
                                                        GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                        auditLog,
                                                        localServerUserId,
                                                        maxPageSize,
                                                        governanceEngineHandlers);

            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.SERVER_INITIALIZED.getMessageDefinition(localServerName));

            return activatedServiceList;
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
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
     * @return map of default engine service configurations
     */
    private Map<String, EngineServiceDefinition> getDefaultEngineServiceConfigs()
    {
        Map<String, EngineServiceDefinition> engineServiceConfigList = new HashMap<>();

        for (EngineServiceDescription engineServiceDescription : EngineServiceDescription.values())
        {
            EngineServiceRegistrationEntry engineServiceRegistrationEntry = OMAGEngineServiceRegistration.getEngineServiceRegistration(engineServiceDescription.getEngineServiceURLMarker());

            if (engineServiceRegistrationEntry != null)
            {
                EngineServiceDefinition engineServiceDefinition = new EngineServiceDefinition(engineServiceRegistrationEntry);

                engineServiceConfigList.put(engineServiceRegistrationEntry.getEngineServiceURLMarker(), engineServiceDefinition);
            }
        }

        return engineServiceConfigList;
    }


    /**
     * Start up the engine services.
     *
     * @param engineServiceDefinitionList       configured engine services
     * @return activatedServiceList          list of engine services running in the server
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private List<String> initializeEngineServices(List<EngineServiceDefinition> engineServiceDefinitionList) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeEngineServices";
        final String actionDescription = "Initialize Engine Services";

        engineServiceAdminList = new ArrayList<>();

        /*
         * Process the list explicitly configured engine services.
         */
        if (engineServiceDefinitionList != null)
        {
            auditLog.logMessage(actionDescription, EngineHostServicesAuditCode.STARTING_ENGINE_SERVICES.getMessageDefinition(localServerName));

            /*
             * Need to count the engine services because of the possibility of deprecated or disabled engine services in the list.
             */
            int configuredEngineServiceCount = 0;
            int enabledEngineServiceCount = 0;

            for (EngineServiceDefinition engineServiceDefinition : engineServiceDefinitionList)
            {
                configuredEngineServiceCount++;

                if (ServiceOperationalStatus.ENABLED.equals(engineServiceDefinition.getEngineServiceOperationalStatus()))
                {
                    enabledEngineServiceCount++;
                    this.setServerServiceActiveStatus(engineServiceDefinition.getEngineServiceFullName(), ServerActiveStatus.STARTING);

                    try
                    {
                        EngineServiceAdmin engineServiceAdmin = this.getEngineServiceAdminClass(engineServiceDefinition);

                        /*
                         * Each engine service has its own audit log instance.
                         */
                        AuditLog engineServicesAuditLog
                                = auditLog.createNewAuditLog(engineServiceDefinition.getEngineServiceId(),
                                                             engineServiceDefinition.getEngineServiceDevelopmentStatus(),
                                                             engineServiceDefinition.getEngineServiceFullName(),
                                                             engineServiceDefinition.getEngineServiceDescription(),
                                                             engineServiceDefinition.getEngineServiceWiki());

                         engineServiceAdmin.initialize(localServerName,
                                                       engineServicesAuditLog,
                                                       localServerUserId,
                                                       maxPageSize,
                                                       engineServiceDefinition);

                        engineServiceAdminList.add(engineServiceAdmin);
                        this.setServerServiceActiveStatus(engineServiceDefinition.getEngineServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                    catch (OMAGConfigurationErrorException error)
                    {
                        auditLog.logException(methodName,
                                              EngineHostServicesAuditCode.ENGINE_SERVICE_INSTANCE_FAILURE.getMessageDefinition(engineServiceDefinition.getEngineServiceName(),
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                              engineServiceDefinition.toString(),
                                              error);
                        throw error;
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              EngineHostServicesAuditCode.ENGINE_SERVICE_INSTANCE_FAILURE.getMessageDefinition(engineServiceDefinition.getEngineServiceName(),
                                                                                                                               error.getClass().getName(),
                                                                                                                               error.getMessage()),
                                              engineServiceDefinition.toString(),
                                              error);

                        throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(localServerName,
                                                                                                                                                       engineServiceDefinition.getEngineServiceName(),
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
                                        EngineHostServicesAuditCode.SKIPPING_ENGINE_SERVICE.getMessageDefinition(engineServiceDefinition.getEngineServiceFullName(),
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
     * @param engineServiceDefinition configuration for the engine service
     * @return Admin class for the engine service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private EngineServiceAdmin getEngineServiceAdminClass(EngineServiceDefinition engineServiceDefinition) throws OMAGConfigurationErrorException
    {
        final String methodName = "getEngineServiceAdminClass";

        String    engineServiceAdminClassName = engineServiceDefinition.getEngineServiceAdminClass();

        if (engineServiceAdminClassName != null)
        {
            try
            {
                return (EngineServiceAdmin) Class.forName(engineServiceAdminClassName).getDeclaredConstructor().newInstance();
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException| NoSuchMethodException | InvocationTargetException error)
            {
                auditLog.logException(methodName,
                                      EngineHostServicesAuditCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(engineServiceDefinition.getEngineServiceName(),
                                                                                                                      engineServiceAdminClassName,
                                                                                                                      error.getMessage()),
                                      engineServiceDefinition.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                                          engineServiceAdminClassName,
                                                                                                                                          engineServiceDefinition.getEngineServiceName()),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      EngineHostServicesAuditCode.BAD_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(engineServiceDefinition.getEngineServiceName(),
                                                                                                                      engineServiceAdminClassName,
                                                                                                                      error.getMessage()),
                                      engineServiceDefinition.toString(),
                                      error);

                throw error;
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                EngineHostServicesAuditCode.NULL_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                 engineServiceDefinition.getEngineServiceFullName()),
                                engineServiceDefinition.toString());

            throw new OMAGConfigurationErrorException(EngineHostServicesErrorCode.NULL_ENGINE_SERVICE_ADMIN_CLASS.getMessageDefinition(localServerName,
                                                                                                                                       engineServiceDefinition.getEngineServiceName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }



}
