/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationGroupConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.frameworkservices.gaf.client.GovernanceConfigurationClient;
import org.odpi.openmetadata.frameworkservices.gaf.client.OIFContextManager;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataEventClient;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorCacheMap;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationGroupHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.GroupConfigurationRefreshThread;
import org.odpi.openmetadata.serveroperations.properties.OMAGServerServiceStatus;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;

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
    private final String localServerName;                  /* Initialized in constructor */
    private final String localServerId;                    /* Initialized in constructor */
    private final String localServerUserId;                /* Initialized in constructor */
    private final int    maxPageSize;                      /* Initialized in constructor */


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
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public IntegrationDaemonOperationalServices(String localServerName,
                                                String localServerId,
                                                String localServerUserId,
                                                int    maxPageSize)
    {
        this.localServerName                  = localServerName;
        this.localServerId                    = localServerId;
        this.localServerUserId                = localServerUserId;
        this.maxPageSize                      = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param dynamicConfiguration config properties for dynamic integration groups
     * @param auditLog destination for audit log messages.
     *
     * @return activated services list
     * @throws OMAGConfigurationErrorException error in configuration preventing startup
     */
    public List<String> initialize(List<IntegrationGroupConfig>   dynamicConfiguration,
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
            if ((dynamicConfiguration == null) || (dynamicConfiguration.isEmpty()))
            {
                throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            IntegrationConnectorCacheMap         daemonConnectorHandlers    = new IntegrationConnectorCacheMap();
            Map<String, IntegrationGroupHandler> integrationGroupHandlerMap = new HashMap<>();

            for (IntegrationGroupConfig integrationGroupConfig : dynamicConfiguration)
            {
                if (integrationGroupConfig != null)
                {
                    String partnerOMASRootURL    = this.getPartnerOMASRootURL(integrationGroupConfig);
                    String partnerOMASServerName = this.getPartnerOMASServerName(integrationGroupConfig);
                    String groupName             = this.getIntegrationGroupName(integrationGroupConfig);

                    EgeriaOpenMetadataEventClient eventClient = new EgeriaOpenMetadataEventClient(partnerOMASServerName,
                                                                                                  partnerOMASRootURL,
                                                                                                  localServerUserId,
                                                                                                  integrationGroupConfig.getSecretsStoreProvider(),
                                                                                                  integrationGroupConfig.getSecretsStoreLocation(),
                                                                                                  integrationGroupConfig.getSecretsStoreCollection(),
                                                                                                  maxPageSize,
                                                                                                  auditLog,
                                                                                                  localServerId+groupName);

                    GovernanceConfigurationClient configurationClient = new GovernanceConfigurationClient(partnerOMASServerName,
                                                                                                          partnerOMASRootURL,
                                                                                                          integrationGroupConfig.getSecretsStoreProvider(),
                                                                                                          integrationGroupConfig.getSecretsStoreLocation(),
                                                                                                          integrationGroupConfig.getSecretsStoreCollection(),
                                                                                                          maxPageSize,
                                                                                                          auditLog);


                    IntegrationContextManager integrationContextManager = new OIFContextManager();

                    integrationContextManager.initializeContextManager(localServerName,
                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(),
                                                                       partnerOMASServerName,
                                                                       partnerOMASRootURL,
                                                                       localServerUserId,
                                                                       integrationGroupConfig.getSecretsStoreProvider(),
                                                                       integrationGroupConfig.getSecretsStoreLocation(),
                                                                       integrationGroupConfig.getSecretsStoreCollection(),
                                                                       maxPageSize,
                                                                       auditLog);

                    integrationContextManager.createClients();

                    IntegrationGroupHandler groupHandler = new IntegrationGroupHandler(integrationGroupConfig.getIntegrationGroupQualifiedName(),
                                                                                       integrationContextManager,
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
                    GroupConfigurationRefreshThread configurationRefreshThread = new GroupConfigurationRefreshThread(integrationGroupConfig,
                                                                                                                     groupHandler,
                                                                                                                     eventClient,
                                                                                                                     auditLog,
                                                                                                                     localServerUserId,
                                                                                                                     localServerName,
                                                                                                                     partnerOMASServerName,
                                                                                                                     partnerOMASRootURL,
                                                                                                                     maxPageSize);
                    configurationRefreshThreads.add(configurationRefreshThread);
                    Thread thread = new Thread(configurationRefreshThread, configurationRefreshThread.getClass().getName());
                    thread.start();
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
                                                                      integrationGroupHandlerMap,
                                                                      daemonConnectorHandlers);

            auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_INITIALIZED.getMessageDefinition(localServerName));

            return new ArrayList<>(serviceStatusMap.keySet());
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
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
