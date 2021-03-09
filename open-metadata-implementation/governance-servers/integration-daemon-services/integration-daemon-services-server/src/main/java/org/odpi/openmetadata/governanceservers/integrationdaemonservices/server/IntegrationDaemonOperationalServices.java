/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.integrationdaemonservices.server;

import org.odpi.openmetadata.adminservices.configuration.properties.IntegrationServiceConfig;
import org.odpi.openmetadata.adminservices.configuration.registration.GovernanceServicesDescription;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesAuditCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.ffdc.IntegrationDaemonServicesErrorCode;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationConnectorHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.handlers.IntegrationServiceHandler;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.threads.IntegrationDaemonThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * IntegrationDaemonOperationalServices is responsible for controlling the startup and shutdown of
 * of the integration daemon services.
 */
public class IntegrationDaemonOperationalServices
{
    private String                         localServerName;               /* Initialized in constructor */
    private String                         localServerUserId;             /* Initialized in constructor */
    private String                         localServerPassword;           /* Initialized in constructor */
    private int                            maxPageSize;                   /* Initialized in constructor */


    private AuditLog                       auditLog     = null;
    private IntegrationDaemonInstance      integrationDaemonInstance = null;

    /**
     * Constructor used at server startup.
     *
     * @param localServerName name of the local server
     * @param localServerUserId user id for this server to use on REST calls if processing inbound messages.
     * @param localServerPassword user password for this server to use on REST calls if processing inbound messages.
     * @param maxPageSize maximum number of records that can be requested on the pageSize parameter
     */
    public IntegrationDaemonOperationalServices(String localServerName,
                                                String localServerUserId,
                                                String localServerPassword,
                                                int    maxPageSize)
    {
        this.localServerName       = localServerName;
        this.localServerUserId     = localServerUserId;
        this.localServerPassword   = localServerPassword;
        this.maxPageSize           = maxPageSize;
    }


    /**
     * Initialize the service.
     *
     * @param configuration config properties
     * @param auditLog destination for audit log messages.
     *
     * @return activated services list
     * @throws OMAGConfigurationErrorException error in configuration preventing startup
     */
    public List<String> initialize(List<IntegrationServiceConfig> configuration,
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
            if (configuration == null)
            {
                throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_CONFIG_DOC.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }
            else if (configuration.isEmpty())
            {
                throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_INTEGRATION_SERVICES_CONFIGURED.getMessageDefinition(localServerName),
                                                          this.getClass().getName(),
                                                          methodName);
            }

            /*
             * Initialize each of the integration services and accumulate the integration connector handlers for the
             * integration daemon handler.
             */
            List<String>                           activatedServicesList = new ArrayList<>();
            List<IntegrationConnectorHandler>      daemonConnectorHandlers = new ArrayList<>();
            Map<String, IntegrationServiceHandler> integrationServiceHandlerMap = new HashMap<>();

            for (IntegrationServiceConfig integrationServiceConfig : configuration)
            {
                if (integrationServiceConfig != null)
                {
                    String                    partnerOMASRootURL          = this.getPartnerOMASRootURL(integrationServiceConfig);
                    String                    partnerOMASServerName       = this.getPartnerOMASServerName(integrationServiceConfig);
                    String                    integrationServiceURLMarker = this.getServiceURLMarker(integrationServiceConfig);
                    IntegrationContextManager contextManager              = this.getContextManager(integrationServiceConfig);

                    if (integrationServiceConfig.getDefaultPermittedSynchronization() == null)
                    {
                        auditLog.logMessage(actionDescription,
                                            IntegrationDaemonServicesAuditCode.NO_PERMITTED_SYNCHRONIZATION.getMessageDefinition(localServerName),
                                            integrationServiceConfig.toString());

                        throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_PERMITTED_SYNCHRONIZATION.getMessageDefinition(localServerName),
                                                                  this.getClass().getName(),
                                                                  methodName);
                    }

                    contextManager.initializeContextManager(partnerOMASServerName,
                                                            partnerOMASRootURL,
                                                            localServerUserId,
                                                            localServerPassword,
                                                            maxPageSize,
                                                            auditLog);

                    contextManager.createClients();

                    IntegrationServiceHandler integrationServiceHandler = new IntegrationServiceHandler(localServerName,
                                                                                                        integrationServiceConfig,
                                                                                                        contextManager,
                                                                                                        auditLog);

                    List<IntegrationConnectorHandler> serviceConnectorHandlers = integrationServiceHandler.initialize();
                    if (serviceConnectorHandlers != null)
                    {
                        daemonConnectorHandlers.addAll(serviceConnectorHandlers);
                    }

                    integrationServiceHandlerMap.put(integrationServiceURLMarker, integrationServiceHandler);
                    activatedServicesList.add(integrationServiceConfig.getIntegrationServiceFullName());
                }
            }

            /*
             * Create the thread that calls refresh on all of the connectors.
             */
            IntegrationDaemonThread integrationDaemonThread = new IntegrationDaemonThread(localServerName,
                                                                                          daemonConnectorHandlers,
                                                                                          auditLog);

            integrationDaemonThread.start();

            /*
             * Create the integration daemon instance.
             */
            integrationDaemonInstance = new IntegrationDaemonInstance(localServerName,
                                                                      GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(),
                                                                      auditLog,
                                                                      localServerUserId,
                                                                      maxPageSize,
                                                                      integrationDaemonThread,
                                                                      integrationServiceHandlerMap);



            auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_INITIALIZED.getMessageDefinition(localServerName));

            return activatedServicesList;
        }
        catch (InvalidParameterException error)
        {
            throw new OMAGConfigurationErrorException(error.getReportedErrorMessage(), error);
        }
        catch (OMAGConfigurationErrorException error)
        {
            throw error;
        }
        catch (Throwable error)
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
            final String methodName        = "getAccessServiceRootURL";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_URL.getMessageDefinition(localServerName));

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
            final String methodName        = "getAccessServiceServerName";

            auditLog.logMessage(actionDescription,
                                IntegrationDaemonServicesAuditCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName));

            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NO_OMAS_SERVER_NAME.getMessageDefinition(localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        return accessServiceServerName;
    }


    /**
     * Return an instance of the context manager class.  This is needed by the integration service handler.
     *
     * @param integrationServiceConfig configuration for the access service
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
                return (IntegrationContextManager) Class.forName(contextManagerClassName).newInstance();
            }
            catch (ClassNotFoundException | InstantiationException | IllegalAccessException error)
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
            catch (Throwable error)
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
                                                              getMessageDefinition(integrationServiceConfig.getIntegrationServiceFullName(),
                                                                                   localServerName),
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

        if ((integrationServiceConfig.getIntegrationServiceName() == null) || (integrationServiceConfig.getIntegrationServiceName().length() == 0))
        {
            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(serviceNamePropertyName,
                                                                                                                                        unknownValue,
                                                                                                                                        localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if ((integrationServiceConfig.getIntegrationServiceFullName() == null) || (integrationServiceConfig.getIntegrationServiceFullName().length() == 0))
        {
            throw new OMAGConfigurationErrorException(IntegrationDaemonServicesErrorCode.NULL_SERVICE_CONFIG_VALUE.getMessageDefinition(serviceFullNamePropertyName,
                                                                                                                                        integrationServiceConfig.getIntegrationServiceName(),
                                                                                                                                        localServerName),
                                                      this.getClass().getName(),
                                                      methodName);
        }

        if ((integrationServiceConfig.getIntegrationServiceURLMarker() == null) || (integrationServiceConfig.getIntegrationServiceURLMarker().length() == 0))
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

        integrationDaemonInstance.shutdown();

        auditLog.logMessage(actionDescription, IntegrationDaemonServicesAuditCode.SERVER_SHUTDOWN.getMessageDefinition(localServerName));
    }
}
