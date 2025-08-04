/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.serveroperations.server;

import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.classifier.ServerTypeClassifier;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.registration.*;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.server.OMAGServerAdminStoreServices;
import org.odpi.openmetadata.adminservices.server.OMAGServerErrorHandler;
import org.odpi.openmetadata.adminservices.server.OMAGServerExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.enginehostservices.registration.OMAGEngineServiceRegistration;
import org.odpi.openmetadata.governanceservers.enginehostservices.server.EngineHostOperationalServices;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonOperationalServices;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.serveroperations.ffdc.ServerOpsAuditCode;
import org.odpi.openmetadata.serveroperations.ffdc.ServerOpsErrorCode;
import org.odpi.openmetadata.serveroperations.properties.ServerActiveStatus;
import org.odpi.openmetadata.serveroperations.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.serveroperations.rest.ServerServicesListResponse;
import org.odpi.openmetadata.serveroperations.rest.SuccessMessageResponse;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMAGServerOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerOperationalServices
{
    private final OMAGServerOperationalInstanceHandler instanceHandler = new OMAGServerOperationalInstanceHandler(CommonServicesDescription.SERVER_OPERATIONS.getServiceName());

    private final OMAGServerPlatformInstanceMap        platformInstanceMap = new OMAGServerPlatformInstanceMap();

    private final OMAGServerAdminStoreServices configStore      = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler     errorHandler     = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler exceptionHandler = new OMAGServerExceptionHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerOperationalServices.class),
                                                                            CommonServicesDescription.SERVER_OPERATIONS.getServiceName());

    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the list of open metadata and governance servers using the stored configuration information.
     * The code works through the list, starting each server in turn.  It stops if one of the servers fails to
     * start and returns the error.  Otherwise, it continues through the list, returning the successful
     * start-up messages.
     *
     * @param userId  user that is issuing the request
     * @param serverNames  list of server names
     * @return success message or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateServerListWithStoredConfig(String       userId,
                                                                     List<String> serverNames)
    {
        String                 startUpMessage = null;
        SuccessMessageResponse response       = new SuccessMessageResponse();

        response.setRelatedHTTPCode(200);

        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    response = activateWithStoredConfig(userId, serverName.trim());

                    if (response.getRelatedHTTPCode() == 200)
                    {
                        String serverStartUpMessage = "OMAG Server '" + serverName + "' successful start , with message: " +
                                response.getSuccessMessage() + System.lineSeparator();
                        if (startUpMessage == null)
                        {
                            startUpMessage = serverStartUpMessage;
                        }
                        else
                        {
                            startUpMessage += serverStartUpMessage;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            startUpMessage = "No OMAG servers listed in startup configuration";
        }

        if (response.getRelatedHTTPCode() == 200)
        {
            response.setSuccessMessage(new Date() + " " + startUpMessage);
        }

        return response;
    }


    /**
     * Activate the open metadata and governance services using the stored configuration information.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return success message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithStoredConfig(String userId,
                                                           String serverName)
    {
        final String methodName = "activateWithStoredConfig";

        SuccessMessageResponse response = new SuccessMessageResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            response = activateWithSuppliedConfig(userId, serverName, configStore.getServerConfigForStartUp(userId, serverName, methodName));
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        return response;
    }




    /**
     * Refresh the definitions of the registered services for this server configuration, so they match the
     * definitions in this platform.
     *
     * @param serverConfig server to refresh
     * @param auditLog operational audit log
     */
    private void refreshRegisteredServices(OMAGServerConfig serverConfig,
                                           AuditLog         auditLog)
    {
        final String methodName = "refreshRegisteredServices";
        /*
         * Refresh the definition of any access service to match the current platform implementation.
         */
        if (serverConfig.getAccessServicesConfig() != null)
        {
            List<AccessServiceConfig> accessServiceConfigList = new ArrayList<>();

            for (AccessServiceConfig accessServiceConfig : serverConfig.getAccessServicesConfig())
            {
                if (accessServiceConfig != null)
                {
                    AccessServiceRegistrationEntry description = OMAGAccessServiceRegistration.getAccessServiceRegistration(accessServiceConfig.getAccessServiceURLMarker());

                    if (description != null)
                    {
                        accessServiceConfig.setAccessServiceName(description.getAccessServiceName());
                        accessServiceConfig.setAccessServiceDevelopmentStatus(description.getAccessServiceDevelopmentStatus());
                        accessServiceConfig.setAccessServiceName(description.getAccessServiceName());
                        accessServiceConfig.setAccessServiceDescription(description.getAccessServiceDescription());
                        accessServiceConfig.setAccessServiceURLMarker(description.getAccessServiceURLMarker());
                        accessServiceConfig.setAccessServiceWiki(description.getAccessServiceWiki());
                        accessServiceConfig.setAccessServiceOperationalStatus(description.getAccessServiceOperationalStatus());

                        accessServiceConfigList.add(accessServiceConfig);
                    }
                    else
                    {
                        auditLog.logMessage(methodName, OMAGAdminAuditCode.IGNORING_UNREGISTERED_SERVICE.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                                              serverConfig.getLocalServerName()));
                    }
                }
            }

            serverConfig.setAccessServicesConfig(accessServiceConfigList);
        }

        /*
         * Refresh the definition of any engine service to match the current platform implementation.
         */
        if (serverConfig.getEngineHostServicesConfig() != null)
        {
            EngineHostServicesConfig engineHostServicesConfig = serverConfig.getEngineHostServicesConfig();

            if (engineHostServicesConfig.getEngineServiceConfigs() != null)
            {
                List<EngineServiceConfig> engineServiceConfigList = new ArrayList<>();

                for (EngineServiceConfig engineServiceConfig : engineHostServicesConfig.getEngineServiceConfigs())
                {
                    if (engineServiceConfig != null)
                    {
                        EngineServiceRegistrationEntry description = OMAGEngineServiceRegistration.getEngineServiceRegistration(engineServiceConfig.getEngineServiceURLMarker());

                        if (description != null)
                        {
                            engineServiceConfig.setEngineServiceName(description.getEngineServiceName());
                            engineServiceConfig.setEngineServiceDevelopmentStatus(description.getEngineServiceDevelopmentStatus());
                            engineServiceConfig.setEngineServiceFullName(description.getEngineServiceFullName());
                            engineServiceConfig.setEngineServiceDescription(description.getEngineServiceDescription());
                            engineServiceConfig.setEngineServiceURLMarker(description.getEngineServiceURLMarker());
                            engineServiceConfig.setEngineServiceWiki(description.getEngineServiceWiki());
                            engineServiceConfig.setEngineServicePartnerOMAS(description.getEngineServicePartnerOMAS());
                            engineServiceConfig.setEngineServiceOperationalStatus(description.getEngineServiceOperationalStatus());

                            engineServiceConfigList.add(engineServiceConfig);
                        }
                        else
                        {
                            auditLog.logMessage(methodName, OMAGAdminAuditCode.IGNORING_UNREGISTERED_SERVICE.getMessageDefinition(engineServiceConfig.getEngineServiceName(),
                                                                                                                                  serverConfig.getLocalServerName()));
                        }
                    }
                }

                engineHostServicesConfig.setEngineServiceConfigs(engineServiceConfigList);
            }
        }

        /*
         * Refresh the definition of any view service to match the current platform implementation.
         */
        if (serverConfig.getViewServicesConfig() != null)
        {
            List<ViewServiceConfig> viewServiceConfigList = new ArrayList<>();

            for (ViewServiceConfig viewServiceConfig : serverConfig.getViewServicesConfig())
            {
                if (viewServiceConfig != null)
                {
                    ViewServiceRegistrationEntry description = OMAGViewServiceRegistration.getViewServiceRegistration(viewServiceConfig.getViewServiceURLMarker());

                    if (description != null)
                    {
                        viewServiceConfig.setViewServiceName(description.getViewServiceName());
                        viewServiceConfig.setViewServiceDevelopmentStatus(description.getViewServiceDevelopmentStatus());
                        viewServiceConfig.setViewServiceFullName(description.getViewServiceFullName());
                        viewServiceConfig.setViewServiceDescription(description.getViewServiceDescription());
                        viewServiceConfig.setViewServiceURLMarker(description.getViewServiceURLMarker());
                        viewServiceConfig.setViewServiceWiki(description.getViewServiceWiki());
                        viewServiceConfig.setViewServicePartnerService(description.getViewServicePartnerService());
                        viewServiceConfig.setViewServiceOperationalStatus(ServiceOperationalStatus.ENABLED);

                        viewServiceConfigList.add(viewServiceConfig);
                    }
                    else
                    {
                        auditLog.logMessage(methodName, OMAGAdminAuditCode.IGNORING_UNREGISTERED_SERVICE.getMessageDefinition(viewServiceConfig.getViewServiceName(),
                                                                                                                              serverConfig.getLocalServerName()));
                    }
                }
            }

            serverConfig.setViewServicesConfig(viewServiceConfigList);
        }
    }


    /**
     * Activate the open metadata and governance services using the supplied configuration
     * document.  Inside the configuration document are sections that each relate
     * to an open metadata and governance subsystem.  This method reads the configuration
     * document, starting up each requested subsystem.  If any subsystem throws an exception,
     * the whole start up process is halted and the exception is returned to the caller.
     *
     * @param userId  user that is issuing the request
     * @param configuration  properties used to initialize the services
     * @param serverName  local server name
     * @return success message response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the server name is invalid or
     * OMAGConfigurationErrorException there is a problem using the supplied configuration.
     */
    public SuccessMessageResponse activateWithSuppliedConfig(String           userId,
                                                             String           serverName,
                                                             OMAGServerConfig configuration)
    {
        final String methodName        = "activateWithSuppliedConfig";
        final String actionDescription = "Initialize OMAG Server subsystems";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        List<String>                    activatedServiceList = new ArrayList<>();
        OMAGOperationalServicesInstance instance             = null;
        SuccessMessageResponse          response             = new SuccessMessageResponse();

        try
        {
            /*
             * Check that a serverName and userId is supplied
             */
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            /*
             * Validate the content of the configuration document.  This will throw an exception if the
             * configuration document is null or the combination of requested services does not make a useful server.
             */
            ServerTypeClassifier     serverTypeClassifier = new ServerTypeClassifier(serverName, configuration);
            ServerTypeClassification serverTypeClassification = serverTypeClassifier.getServerType();
            if (configuration.getLocalServerType() == null)
            {
                configuration.setLocalServerType(serverTypeClassification.getServerTypeName());
            }

            /*
             * Validate that the server is not running already.  If it is running it should be shutdown.
             */
            if (instanceHandler.isServerActive(userId, serverName))
            {
                this.shutdownServer(userId, serverName);
            }

            /*
             * The instance saves the operational services objects for this server instance, so they can be retrieved
             * in response to subsequent REST calls for the server.  These instances provide the multi-tenant
             * support in Egeria.
             */
            instance = new OMAGOperationalServicesInstance(serverName,
                                                           serverTypeClassification,
                                                           CommonServicesDescription.SERVER_OPERATIONS.getServiceName(),
                                                           configuration.getMaxPageSize());
            instance.setServerActiveStatus(ServerActiveStatus.STARTING);

            /*
             * Save the configuration that is going to be used to start the server for this instance.  This configuration can be queried by
             * the operator to verify the configuration used to start the server. (The values in the config store may have been
             * updated since the server was started.)
             */
            instance.setOperationalConfiguration(configuration);

            /* ================================
             * Ready to start subsystems.  A failure in startup for any subsystem is fatal.
             */

            /*
             * Initialize the open metadata repository services first since other services depend on it.
             * (Even the governance servers need the audit log.)
             */
            OMRSOperationalServices         operationalRepositoryServices;

            instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.STARTING);
            operationalRepositoryServices = new OMRSOperationalServices(configuration.getLocalServerName(),
                                                                        configuration.getLocalServerType(),
                                                                        configuration.getOrganizationName(),
                                                                        configuration.getLocalServerUserId(),
                                                                        configuration.getLocalServerPassword(),
                                                                        configuration.getLocalServerURL(),
                                                                        configuration.getMaxPageSize());
            activatedServiceList.add(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName());
            operationalRepositoryServices.initializeAuditLog(configuration.getRepositoryServicesConfig(),
                                                             serverTypeClassification.getServerTypeName());



            /*
             * Create an audit log for logging initialization progress and errors.
             * Each subsystem should be logging the start-up of their components and handling
             * their errors.  However, the logging and error handling done by this method is to bracket the
             * start-up of the different types of subsystems and provide minimal diagnostics for
             * immature subsystems that have not yet developed their logging and error handling.
             */
            OMRSAuditLog auditLog = operationalRepositoryServices.getAuditLog(
                    CommonServicesDescription.SERVER_OPERATIONS.getServiceCode(),
                    CommonServicesDescription.SERVER_OPERATIONS.getServiceDevelopmentStatus(),
                    CommonServicesDescription.SERVER_OPERATIONS.getServiceName(),
                    CommonServicesDescription.SERVER_OPERATIONS.getServiceDescription(),
                    CommonServicesDescription.SERVER_OPERATIONS.getServiceWiki());
            instance.setAuditLog(auditLog);

            /*
             * Check that the description of the services reflect the latest information about the registered services.
             */
            refreshRegisteredServices(configuration, auditLog);

            /*
             * There are many paging services in Egeria.  This value sets a maximum page size that a requester can use.
             * It is passed to each subsystem at start-up, so it can enforce the limit on all paging REST calls.
             * Having a limit helps to prevent a denial of service attack that uses very large requests to overwhelm the server.
             * If this value is 0 it means there is no upper limit.  If this value is negative then it is invalid.
             */
            this.validateMaxPageSize(configuration.getMaxPageSize(), serverName, auditLog);

            /*
             * Save the instance of the repository services and then initialize it.  OMRS has 2 modes of initialization.
             * Firstly for a basic server such as a governance server, just the audit log is initialized.
             * For a metadata server, repository proxy and conformance test server, initialization will optionally set up the
             * connector to the local repository, initialize the enterprise repository services (used by
             * the access services and conformance test server) and connect to the server's cohorts.   It is controlled by the settings in the
             * repository services configuration document.  The repository services instance is saved since it needs to be called for shutdown.
             */
            instance.setOperationalRepositoryServices(operationalRepositoryServices);

            /*
             * At this point the type of server influences the start-up sequence.
             */
            if ((ServerTypeClassification.METADATA_ACCESS_STORE.equals(serverTypeClassification)) ||
                (ServerTypeClassification.METADATA_ACCESS_SERVER.equals(serverTypeClassification)) ||
                (ServerTypeClassification.REPOSITORY_PROXY.equals(serverTypeClassification)) ||
                (ServerTypeClassification.CONFORMANCE_SERVER.equals(serverTypeClassification)))
            {
                /*
                 * This server is a source of metadata and is capable of joining an open metadata repository cohort.
                 */
                operationalRepositoryServices.initializeCohortMember(configuration.getRepositoryServicesConfig());

                /*
                 * Set up the server instance - ensure it is active and the security has been set up correctly.
                 */
                OpenMetadataServerSecurityVerifier securityVerifier =
                        platformInstanceMap.startUpServerInstance(configuration.getLocalServerUserId(),
                                                                  serverName,
                                                                  operationalRepositoryServices.getAuditLog(CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceCode(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDevelopmentStatus(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceName(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDescription(),
                                                                                                            CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceWiki()),
                                                                  configuration.getServerSecurityConnection());

                /*
                 * Pass the resulting security verifier to the repository services.  It will be set up in the local
                 * repository (if there is a local repository in this server).  This is the point where we connect to the cohort.
                 */
                operationalRepositoryServices.setSecurityVerifier(securityVerifier);
                instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);

                /*
                 * Now initialize the configured open metadata access services.  Each access service has its own subsystem.  It is
                 * initialized via an Admin object that controls its start up and shutdown.  The configuration service just needs to create the
                 * appropriate admin object (specified in the configuration) and initialize it with its own configuration
                 * document.  The admin object then does the rest.  The admin objects are stored in the instance since
                 * they also need to be called for shutdown.
                 *
                 * Each access service is given access to the events from open metadata repository cohorts that this server connects to.
                 * The enterprise topic connector supplies these events.  The access service registers a listener with it to receive them.
                 */
                initializeAccessServices(instance,
                                         configuration.getAccessServicesConfig(),
                                         operationalRepositoryServices,
                                         operationalRepositoryServices.getEnterpriseOMRSTopicConnector(),
                                         configuration.getLocalServerUserId(),
                                         configuration.getLocalServerPassword(),
                                         serverName,
                                         activatedServiceList,
                                         auditLog,
                                         configuration.getMaxPageSize());

                /*
                 * Initialize the Open Metadata Conformance Suite Services.  This runs the Open Metadata TestLabs that are
                 * part of the ODPi Egeria Conformance Program.
                 */
                if (ServerTypeClassification.CONFORMANCE_SERVER.equals(serverTypeClassification))
                {
                    ConformanceSuiteOperationalServices
                            operationalConformanceSuiteServices = new ConformanceSuiteOperationalServices(configuration.getLocalServerName(),
                                                                                                          configuration.getLocalServerUserId(),
                                                                                                          configuration.getLocalServerPassword(),
                                                                                                          configuration.getMaxPageSize());
                    instance.setOperationalConformanceSuiteServices(operationalConformanceSuiteServices);
                    operationalConformanceSuiteServices.initialize(configuration.getConformanceSuiteConfig(),
                                                                   operationalRepositoryServices.getEnterpriseOMRSTopicConnector(),
                                                                   operationalRepositoryServices.getEnterpriseConnectorManager(),
                                                                   operationalRepositoryServices.getAuditLog(
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceCode(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceDevelopmentStatus(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceDescription(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName());
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);
                }

                /*
                 * The enterprise topic passes OMRS Events from the cohort to the listening access services.
                 * During the access services start up, they registered listeners with the enterprise topic.
                 * Starting the enterprise topic will start the flow of events to the registered access services.
                 */
                if (operationalRepositoryServices.getEnterpriseOMRSTopicConnector() != null)
                {
                    try
                    {
                        operationalRepositoryServices.getEnterpriseOMRSTopicConnector().start();
                    }
                    catch (Exception  error)
                    {
                        throw new OMAGConfigurationErrorException(ServerOpsErrorCode.ENTERPRISE_TOPIC_START_FAILED.getMessageDefinition(serverName,
                                                                                                                                        "in memory",
                                                                                                                                        error.getClass().getName(),
                                                                                                                                        error.getMessage()),
                                                                  this.getClass().getName(),
                                                                  methodName);
                    }
                }

                /*
                 * Start publishing events from the local repository.
                 */
                operationalRepositoryServices.startOutboundEvents();
            }

            else if (ServerTypeClassification.VIEW_SERVER.equals(serverTypeClassification))
            {
                /*
                 * Set up the repository services REST API
                 */
                operationalRepositoryServices.initializeViewServer(configuration.getRepositoryServicesConfig());
                instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);

                /*
                 * Set up the server instance - ensure it is active and the security has been set up correctly.
                 */
                platformInstanceMap.startUpServerInstance(configuration.getLocalServerUserId(),
                                                          serverName,
                                                          operationalRepositoryServices.getAuditLog(
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceCode(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDevelopmentStatus(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceName(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDescription(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceWiki()),
                                                          configuration.getServerSecurityConnection());


                /*
                 * Set up the view services that are the speciality of the view server.
                 */
                initializeViewServices(instance,
                                       configuration.getViewServicesConfig(),
                                       operationalRepositoryServices,
                                       configuration.getLocalServerUserId(),
                                       configuration.getLocalServerPassword(),
                                       serverName,
                                       activatedServiceList,
                                       configuration.getMaxPageSize(),
                                       auditLog);
            }
            else /* governance servers */
            {
                /*
                 * Set up the repository services REST API
                 */
                operationalRepositoryServices.initializeGovernanceServer(configuration.getRepositoryServicesConfig());
                instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);

                /*
                 * Governance servers are varied in nature.  Many host connectors that exchange metadata with third party technologies.
                 * However, they may also host specific types of engines, or provide an implementation of a complete governance service.
                 * Because of this variety, Egeria does not (yet) provide any specialist frameworks for supporting the governance servers.
                 * All the implementation is in the governance services subsystems initialized below.
                 *
                 * Set up the server instance - ensure it is active and the security has been set up correctly.
                 */
                platformInstanceMap.startUpServerInstance(configuration.getLocalServerUserId(),
                                                          serverName,
                                                          operationalRepositoryServices.getAuditLog(
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceCode(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDevelopmentStatus(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceName(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceDescription(),
                                                                  CommonServicesDescription.OPEN_METADATA_SECURITY.getServiceWiki()),
                                                          configuration.getServerSecurityConnection());


                /*
                 * Start up the governance services subsystem.   Each type of governance server has its own type of governance services
                 * subsystem.  Each is responsible for handling its own errors.  The error handling that follows helps to position
                 * where any issues are occurring.
                 */
                try
                {
                    auditLog.logMessage(actionDescription,
                                        ServerOpsAuditCode.STARTING_GOVERNANCE_SERVICES.getMessageDefinition(serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                             serverName));

                    initializeGovernanceServices(instance,
                                                 configuration,
                                                 serverTypeClassification,
                                                 operationalRepositoryServices,
                                                 activatedServiceList);

                    auditLog.logMessage(actionDescription,
                                        ServerOpsAuditCode.GOVERNANCE_SERVICES_STARTED.getMessageDefinition(serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                            serverName));
                }
                catch (OMAGConfigurationErrorException  error)
                {
                    /*
                     * There is a configuration error that means that the governance services subsystem can not start.  Since this is
                     * the primary function of the server then there is no purpose in continuing.
                     */
                    auditLog.logException(actionDescription,
                                          ServerOpsAuditCode.GOVERNANCE_SERVICE_FAILURE.getMessageDefinition(error.getClass().getName(),
                                                                                                             serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                             serverName,
                                                                                                             error.getReportedErrorMessage()),
                                          error);
                    throw error;
                }
                catch (Exception  error)
                {
                    /*
                     * Uncontrolled error from the governance service subsystem.  The subsystem could be in any state.
                     * Capture additional information about the error and stop the server startup.
                     */
                    auditLog.logException(actionDescription,
                                          ServerOpsAuditCode.GOVERNANCE_SERVICE_FAILURE.getMessageDefinition(error.getClass().getName(),
                                                                                                             serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                             serverName,
                                                                                                             error.getMessage()),
                                          error);
                    throw error;
                }
            }

            /*
             * All subsystems are started - just log messages and return.
             */
            instance.setServerActiveStatus(ServerActiveStatus.RUNNING);

            String successMessage = new Date() + " " + serverName + " is running the following services: " + activatedServiceList;

            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.SERVER_STARTUP_SUCCESS.getMessageDefinition(serverName,
                                                                                               activatedServiceList.toString()));

            response.setSuccessMessage(successMessage);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, instance);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, instance);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, instance);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, instance);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
            cleanUpRunningServiceInstances(userId, serverName, instance);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * There are many paging services in Egeria.  This value sets a maximum page size that a requester can use.
     * It is passed to each component at start up so each can enforce it on all REST calls.
     * This helps to prevent a denial of service attack that uses very large requests to overwhelm the server.
     * If this value is 0 it means there is no upper limit.  If this value is negative then it isd invalid.
     *
     * @param maxPageSize value to validate
     * @param serverName name of the server that the configuration comes from
     * @param auditLog logging destination
     * @throws OMAGConfigurationErrorException the max page size is negative.
     */
    private void validateMaxPageSize(int          maxPageSize,
                                     String       serverName,
                                     OMRSAuditLog auditLog) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "Validating max page size during server initialization";

        if (maxPageSize > 0)
        {
            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)));
        }
        else if (maxPageSize == 0)
        {
            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.UNLIMITED_MAX_PAGE_SIZE.getMessageDefinition(serverName));
        }
        else
        {
            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.INVALID_MAX_PAGE_SIZE.getMessageDefinition(serverName,
                                                                                              Integer.toString(maxPageSize)));

            throw new OMAGConfigurationErrorException(ServerOpsErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName,
                                                                                                                Integer.toString(maxPageSize)),
                                                      this.getClass().getName(),
                                                      actionDescription);
        }
    }


    /**
     * Start up the access services.
     *
     * @param instance server instance
     * @param accessServiceConfigList configured access services
     * @param operationalRepositoryServices repository services
     * @param enterpriseTopicConnector events from the cohort
     * @param localServerUserId servers userId
     * @param localServerPassword  password to use on OMRS calls where there is no end user.
     * @param localServerName server name
     * @param activatedServiceList list of services (subsystems) running in the server
     * @param maxPageSize max number of results to return on single request.
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private void initializeAccessServices(OMAGOperationalServicesInstance instance,
                                          List<AccessServiceConfig>       accessServiceConfigList,
                                          OMRSOperationalServices         operationalRepositoryServices,
                                          OMRSTopicConnector              enterpriseTopicConnector,
                                          String                          localServerUserId,
                                          String                          localServerPassword,
                                          String                          localServerName,
                                          List<String>                    activatedServiceList,
                                          OMRSAuditLog                    auditLog,
                                          int                             maxPageSize) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeAccessServices";
        final String actionDescription = "Initialize Access Services";

        List<AccessServiceAdmin> operationalAccessServiceAdminList = instance.getOperationalAccessServiceAdminList();
        if (accessServiceConfigList != null)
        {
            auditLog.logMessage(actionDescription, ServerOpsAuditCode.STARTING_ACCESS_SERVICES.getMessageDefinition());

            /*
             * Need to count the access services because of the possibility of deprecated or disabled access services in the list.
             */
            int  configuredAccessServiceCount = 0;
            int  enabledAccessServiceCount = 0;

            for (AccessServiceConfig  accessServiceConfig : accessServiceConfigList)
            {
                if (accessServiceConfig != null)
                {
                    configuredAccessServiceCount ++;

                    if (ServiceOperationalStatus.ENABLED.equals(accessServiceConfig.getAccessServiceOperationalStatus()))
                    {
                        enabledAccessServiceCount ++;
                        instance.setServerServiceActiveStatus(accessServiceConfig.getAccessServiceName(), ServerActiveStatus.STARTING);

                        try
                        {
                            AccessServiceAdmin accessServiceAdmin = this.getAccessServiceAdminClass(accessServiceConfig, auditLog, localServerName);

                            accessServiceAdmin.setFullServiceName(accessServiceConfig.getAccessServiceName());


                            /*
                             * Each access service has its own audit log instance.
                             */
                            AuditLog accessServicesAuditLog
                                    = operationalRepositoryServices.getAuditLog(accessServiceConfig.getAccessServiceId(),
                                                                                accessServiceConfig.getAccessServiceDevelopmentStatus(),
                                                                                accessServiceConfig.getAccessServiceName(),
                                                                                accessServiceConfig.getAccessServiceDescription(),
                                                                                accessServiceConfig.getAccessServiceWiki());

                            /*
                             * We will switch to the new version of this method once all access services have move from using OMRSAuditLog to
                             * AuditLog.  The default implementation of this method delegates to the new version of the method so
                             */
                            accessServiceAdmin.initialize(accessServiceConfig,
                                                          enterpriseTopicConnector,
                                                          operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(accessServiceConfig.getAccessServiceName()),
                                                          accessServicesAuditLog,
                                                          localServerName,
                                                          localServerUserId,
                                                          localServerPassword,
                                                          maxPageSize);
                            operationalAccessServiceAdminList.add(accessServiceAdmin);
                            activatedServiceList.add(accessServiceConfig.getAccessServiceName());
                            instance.setServerServiceActiveStatus(accessServiceAdmin.getFullServiceName(), ServerActiveStatus.RUNNING);
                        }
                        catch (OMAGConfigurationErrorException error)
                        {
                            auditLog.logException(methodName,
                                                  ServerOpsAuditCode.ACCESS_SERVICE_INSTANCE_FAILURE.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                                          error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  error);
                            throw error;
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(methodName,
                                                  ServerOpsAuditCode.ACCESS_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                          accessServiceConfig.getAccessServiceName(),
                                                                                                                          error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  error);

                            throw new OMAGConfigurationErrorException(
                                    ServerOpsErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(localServerName,
                                                                                                                accessServiceConfig.getAccessServiceName(),
                                                                                                                error.getMessage()),
                                    this.getClass().getName(),
                                    methodName,
                                    error);
                        }
                    }
                    else
                    {
                        auditLog.logMessage(actionDescription,
                                            ServerOpsAuditCode.SKIPPING_ACCESS_SERVICE.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                            localServerName));
                    }
                }
            }

            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.ALL_ACCESS_SERVICES_STARTED.getMessageDefinition(Integer.toString(enabledAccessServiceCount),
                                                                                                    Integer.toString(configuredAccessServiceCount)));
        }

        /*
         * Save the list of running access services to the instance and then add the instance to the instance map.
         * The instance information can then be retrieved for shutdown or other management requests.
         */
        instance.setOperationalAccessServiceAdminList(operationalAccessServiceAdminList);
    }



    /**
     * Start up the view services.
     *
     * @param instance                      server instance
     * @param viewServiceConfigList         configured view services
     * @param operationalRepositoryServices repository services
     * @param localServerUserId             servers userId
     * @param serverName                    server name
     * @param localServerPassword           server password or null
     * @param activatedServiceList          list of services (subsystems) running in the server
     * @param maxPageSize                   maximum page size. 0 means unlimited
     * @param auditLog                      audit log
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private void initializeViewServices(OMAGOperationalServicesInstance instance,
                                        List<ViewServiceConfig>         viewServiceConfigList,
                                        OMRSOperationalServices         operationalRepositoryServices,
                                        String                          localServerUserId,
                                        String                          localServerPassword,
                                        String                          serverName,
                                        List<String>                    activatedServiceList,
                                        int                             maxPageSize,
                                        OMRSAuditLog                    auditLog) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeViewServices";
        final String actionDescription = "Initialize View Services";

        List<ViewServiceAdmin>              operationalViewServiceAdminList = instance.getOperationalViewServiceAdminList();
        List<ViewServerGenericServiceAdmin> operationalViewServerGenericServiceAdminList = instance.getOperationalViewServerGenericServiceAdminList();
        if (viewServiceConfigList != null)
        {
            auditLog.logMessage(actionDescription, ServerOpsAuditCode.STARTING_VIEW_SERVICES.getMessageDefinition());

            List<ViewServiceRegistrationEntry> genericServices = new ArrayList<>();
            Map<String, Object>                registeredAdminClasses = new HashMap<>();

            /*
             * Determine which view services always need to be activated
             */
            for (ViewServiceRegistrationEntry registrationEntry : OMAGViewServiceRegistration.getViewServiceRegistrationList())
            {
                if (registrationEntry.getViewServiceOperationalStatus() == ServiceOperationalStatus.ENABLED)
                {
                    try
                    {
                        Object adminClass = this.getViewServiceAdminClass(registrationEntry.getViewServiceName(), registrationEntry.getViewServiceAdminClassName(), auditLog, serverName);

                        registeredAdminClasses.put(registrationEntry.getViewServiceName(), adminClass);

                        if (adminClass instanceof ViewServerGenericServiceAdmin)
                        {
                            genericServices.add(registrationEntry);
                        }
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              ServerOpsAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                    registrationEntry.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              error);

                        throw new OMAGConfigurationErrorException(
                                ServerOpsErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(serverName,
                                                                                                            registrationEntry.getViewServiceName(),
                                                                                                            error.getMessage()),
                                this.getClass().getName(),
                                methodName,
                                error);
                    }
                }
            }

            /*
             * Need to count the view services because of the possibility of deprecated or disabled view services in the list.
             */
            int configuredViewServiceCount = 0;
            int enabledViewServiceCount = 0;
            List<ViewServiceConfig> activeViewServices = new ArrayList<>();
            String partnerServerName = null;
            String partnerPlatformURLRoot = null;

            for (ViewServiceConfig viewServiceConfig : viewServiceConfigList)
            {
                configuredViewServiceCount++;

                if (ServiceOperationalStatus.ENABLED.equals(viewServiceConfig.getViewServiceOperationalStatus()))
                {
                    enabledViewServiceCount++;
                    instance.setServerServiceActiveStatus(viewServiceConfig.getViewServiceFullName(), ServerActiveStatus.STARTING);
                    activeViewServices.add(viewServiceConfig);

                    /*
                     * Capture a server name/platform url root to act as defaults for the generic service.
                     */
                    if (viewServiceConfig.getOMAGServerName() != null)
                    {
                        partnerServerName = viewServiceConfig.getOMAGServerName();
                    }
                    if (viewServiceConfig.getOMAGServerPlatformRootURL() != null)
                    {
                        partnerPlatformURLRoot = viewServiceConfig.getOMAGServerPlatformRootURL();
                    }

                    try
                    {
                        Object adminClass = registeredAdminClasses.get(viewServiceConfig.getViewServiceName());

                        /*
                         * Each view service has its own audit log instance.
                         */
                        OMRSAuditLog viewServicesAuditLog
                                = operationalRepositoryServices.getAuditLog(viewServiceConfig.getViewServiceId(),
                                                                            viewServiceConfig.getViewServiceDevelopmentStatus(),
                                                                            viewServiceConfig.getViewServiceFullName(),
                                                                            viewServiceConfig.getViewServiceDescription(),
                                                                            viewServiceConfig.getViewServiceWiki());

                        if (adminClass instanceof ViewServiceAdmin viewServiceAdmin)
                        {
                            viewServiceAdmin.initialize(serverName,
                                                        viewServiceConfig,
                                                        viewServicesAuditLog,
                                                        localServerUserId,
                                                        localServerPassword,
                                                        maxPageSize);
                            operationalViewServiceAdminList.add(viewServiceAdmin);
                        }
                        else if (adminClass instanceof ViewServerGenericServiceAdmin viewServerGenericServiceAdmin)
                        {
                            viewServerGenericServiceAdmin.initialize(serverName,
                                                                     viewServiceConfig,
                                                                     viewServicesAuditLog,
                                                                     localServerUserId,
                                                                     localServerPassword,
                                                                     maxPageSize,
                                                                     activeViewServices);
                            operationalViewServerGenericServiceAdminList.add(viewServerGenericServiceAdmin);
                        }

                        activatedServiceList.add(viewServiceConfig.getViewServiceFullName());
                        instance.setServerServiceActiveStatus(viewServiceConfig.getViewServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                    catch (OMAGConfigurationErrorException error)
                    {
                        auditLog.logException(methodName,
                                              ServerOpsAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(viewServiceConfig.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              viewServiceConfig.toString(),
                                              error);
                        throw error;
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              ServerOpsAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                    viewServiceConfig.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              viewServiceConfig.toString(),
                                              error);

                        throw new OMAGConfigurationErrorException(
                                ServerOpsErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(serverName,
                                                                                                            viewServiceConfig.getViewServiceName(),
                                                                                                            error.getMessage()),
                                this.getClass().getName(),
                                methodName,
                                error);
                    }
                }
                else
                {
                    auditLog.logMessage(actionDescription,
                                        ServerOpsAuditCode.SKIPPING_VIEW_SERVICE.getMessageDefinition(viewServiceConfig.getViewServiceFullName(),
                                                                                                      serverName));
                }
            }

            /*
             * This final loop activates the generic services that have not been activated through configuration.
             * The process is the same except these services do not appear in the active services lists.
             */
            for (ViewServiceRegistrationEntry genericService : genericServices)
            {
                boolean alreadyActive = false;
                for (ViewServiceConfig viewServiceConfig : activeViewServices)
                {
                    if (viewServiceConfig.getViewServiceFullName().equals(genericService.getViewServiceFullName()))
                    {
                        alreadyActive = true;
                        break;
                    }
                }

                if (! alreadyActive)
                {
                    try
                    {
                        ViewServiceConfig viewServiceConfig = new ViewServiceConfig(genericService);

                        /*
                         * These values are extracted from one of the configured view services.
                         * If there are no configured view services, these values are null and the
                         * startup will fail.
                         */
                        viewServiceConfig.setOMAGServerName(partnerServerName);
                        viewServiceConfig.setOMAGServerPlatformRootURL(partnerPlatformURLRoot);

                        ViewServerGenericServiceAdmin viewServerGenericServiceAdmin = (ViewServerGenericServiceAdmin) registeredAdminClasses.get(viewServiceConfig.getViewServiceName());

                        /*
                         * Each view service has its own audit log instance.
                         */
                        OMRSAuditLog viewServicesAuditLog
                                = operationalRepositoryServices.getAuditLog(viewServiceConfig.getViewServiceId(),
                                                                            viewServiceConfig.getViewServiceDevelopmentStatus(),
                                                                            viewServiceConfig.getViewServiceFullName(),
                                                                            viewServiceConfig.getViewServiceDescription(),
                                                                            viewServiceConfig.getViewServiceWiki());

                        viewServerGenericServiceAdmin.initialize(serverName,
                                                                 viewServiceConfig,
                                                                 viewServicesAuditLog,
                                                                 localServerUserId,
                                                                 localServerPassword,
                                                                 maxPageSize,
                                                                 activeViewServices);
                        operationalViewServerGenericServiceAdminList.add(viewServerGenericServiceAdmin);

                        activatedServiceList.add(viewServiceConfig.getViewServiceFullName());
                        instance.setServerServiceActiveStatus(viewServiceConfig.getViewServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              ServerOpsAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                    genericService.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              genericService.toString(),
                                              error);

                        throw new OMAGConfigurationErrorException(
                                ServerOpsErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(serverName,
                                                                                                            genericService.getViewServiceName(),
                                                                                                            error.getMessage()),
                                this.getClass().getName(),
                                methodName,
                                error);
                    }
                }
            }

            auditLog.logMessage(actionDescription,
                                ServerOpsAuditCode.ALL_VIEW_SERVICES_STARTED.getMessageDefinition(Integer.toString(enabledViewServiceCount),
                                                                                                  Integer.toString(configuredViewServiceCount)));
        }

        /*
         * Save the list of running view services to the instance and then add the instance to the instance map.
         * The instance information can then be retrieved for shutdown or other management requests.
         */
        instance.setOperationalViewServiceAdminList(operationalViewServiceAdminList);
        instance.setOperationalViewServerGenericServiceAdminList(operationalViewServerGenericServiceAdminList);
    }


    /**
     * Create an instance of the access service's admin class from the class name in the configuration.
     *
     * @param accessServiceConfig configuration for the access service
     * @param auditLog logging destination
     * @param serverName this server instance
     * @return Admin class for the access service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private AccessServiceAdmin getAccessServiceAdminClass(AccessServiceConfig   accessServiceConfig,
                                                          OMRSAuditLog          auditLog,
                                                          String                serverName) throws OMAGConfigurationErrorException
    {
        final String methodName = "getAccessServiceAdminClass";

        String    accessServiceAdminClassName = accessServiceConfig.getAccessServiceAdminClass();

        if (accessServiceAdminClassName != null)
        {
            try
            {
                return (AccessServiceAdmin) Class.forName(accessServiceAdminClassName).getDeclaredConstructor().newInstance();
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      ServerOpsAuditCode.BAD_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                             accessServiceAdminClassName,
                                                                                                             error.getMessage()),
                                      accessServiceConfig.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(ServerOpsErrorCode.BAD_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                                 accessServiceAdminClassName,
                                                                                                                                 accessServiceConfig.getAccessServiceName()),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                ServerOpsAuditCode.NULL_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                        accessServiceConfig.getAccessServiceName()),
                                accessServiceConfig.toString());

            throw new OMAGConfigurationErrorException(ServerOpsErrorCode.NULL_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                              accessServiceConfig.getAccessServiceName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Get the View Service admin class for a named server's view service configuration.
     *
     * @param viewServiceName Name for the view service
     * @param viewServiceAdminClassName admin class for the view service
     * @param auditLog logging destination
     * @param serverName this server instance
     * @return Admin class for the view service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private Object getViewServiceAdminClass(String       viewServiceName,
                                            String       viewServiceAdminClassName,
                                            OMRSAuditLog auditLog,
                                            String       serverName) throws OMAGConfigurationErrorException
    {
        final String methodName = "getViewServiceAdminClass";

        if (viewServiceAdminClassName != null)
        {
            try
            {
                return Class.forName(viewServiceAdminClassName).getDeclaredConstructor().newInstance();
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      ServerOpsAuditCode.BAD_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(viewServiceName,
                                                                                                           viewServiceAdminClassName,
                                                                                                           error.getMessage()),
                                      error);

                throw new OMAGConfigurationErrorException(ServerOpsErrorCode.BAD_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                               viewServiceAdminClassName,
                                                                                                                               viewServiceName),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                ServerOpsAuditCode.NULL_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName, viewServiceName));

            throw new OMAGConfigurationErrorException(ServerOpsErrorCode.NULL_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName, viewServiceName),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Initialize the appropriate governance service subsystem for a governance server.
     *
     * @param instance this server's instance object where information about the governance service subsystem is stashed.
     * @param configuration whole server configuration document.
     * @param serverTypeClassification what type of server is this
     * @param operationalRepositoryServices the open metadata repository services (OMRS) instance - use to manufacture audit logs.
     * @param activatedServiceList current list of activated services to append to.
     * @throws OMAGConfigurationErrorException exception throw if governance service subsystem discovered a fatal error.
     *         It will cause the server startup to fail.
     */
    private void initializeGovernanceServices(OMAGOperationalServicesInstance instance,
                                              OMAGServerConfig                configuration,
                                              ServerTypeClassification        serverTypeClassification,
                                              OMRSOperationalServices         operationalRepositoryServices,
                                              List<String>                    activatedServiceList) throws OMAGConfigurationErrorException
    {
        /*
         * Initialize the Engine Host Services for the Engine Host OMAG server.  This is a governance server for running governance engines.
         */
        if (ServerTypeClassification.ENGINE_HOST.equals(serverTypeClassification))
        {
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(), ServerActiveStatus.STARTING);

            EngineHostOperationalServices engineHostOperationalServices
                    = new EngineHostOperationalServices(configuration.getLocalServerName(),
                                                        configuration.getLocalServerId(),
                                                        configuration.getLocalServerUserId(),
                                                        configuration.getLocalServerPassword(),
                                                        configuration.getMaxPageSize());

            instance.setOperationalEngineHost(engineHostOperationalServices);
            List<String> engineServices = engineHostOperationalServices.initialize(configuration.getEngineHostServicesConfig(),
                                                            operationalRepositoryServices.getAuditLog(
                                                                    GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceCode(),
                                                                    GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceDevelopmentStatus(),
                                                                    GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(),
                                                                    GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceDescription(),
                                                                    GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceWiki()));

            activatedServiceList.addAll(engineServices);
            activatedServiceList.add(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName());
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);
        }

        /*
         * Initialize the Integration Daemon Services for the Integration Daemon OMAG Server.  This is a governance server for exchanging
         * metadata with third party technologies.
         */
        else if (ServerTypeClassification.INTEGRATION_DAEMON.equals(serverTypeClassification))
        {
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(), ServerActiveStatus.STARTING);

            IntegrationDaemonOperationalServices integrationDaemonOperationalServices
                    = new IntegrationDaemonOperationalServices(configuration.getLocalServerName(),
                                                               configuration.getLocalServerId(),
                                                               configuration.getLocalServerUserId(),
                                                               configuration.getLocalServerPassword(),
                                                               configuration.getMaxPageSize());

            instance.setOperationalIntegrationDaemon(integrationDaemonOperationalServices);
            List<String> integrationServices = integrationDaemonOperationalServices.initialize(configuration.getDynamicIntegrationGroupsConfig(),
                                                                                               operationalRepositoryServices.getAuditLog(
                                                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceCode(),
                                                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceDevelopmentStatus(),
                                                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(),
                                                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceDescription(),
                                                                                                       GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceWiki()));

            activatedServiceList.addAll(integrationServices);
            activatedServiceList.add(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName());
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);
        }
    }


    /**
     * Called when server start up fails.  The aim is to clean up any partially running services.
     * Any exceptions are ignored as the real cause of the error is already captured.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param instance a list of the running services
     */
    private void cleanUpRunningServiceInstances(String                          userId,
                                                String                          serverName,
                                                OMAGOperationalServicesInstance instance)
    {
        final String methodName = "cleanUpRunningServiceInstances";

        try
        {
            deactivateRunningServiceInstances(userId, serverName, methodName, instance, false);
        }
        catch (Exception  error)
        {
            /*
             * Ignore exception as real cause of error is already caught.
             */
        }
    }


    /**
     * Shutdown any running services for a specific server instance.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param methodName calling method
     * @param instance a list of the running services
     * @param permanentDeactivation should the server be permanently disconnected
     * @throws InvalidParameterException one of the services detected an invalid parameter
     * @throws PropertyServerException one of the services had problems shutting down
     */
    public  void deactivateRunningServiceInstances(String                          userId,
                                                   String                          serverName,
                                                   String                          methodName,
                                                   OMAGOperationalServicesInstance instance,
                                                   boolean                         permanentDeactivation) throws InvalidParameterException,
                                                                                                                 PropertyServerException
    {
        final String actionDescription = "Shutdown server";

        if (instance != null)
        {
            OMRSAuditLog auditLog = instance.getAuditLog();

            if (auditLog != null)
            {
                auditLog.logMessage(actionDescription,
                                    ServerOpsAuditCode.SERVER_SHUTDOWN_STARTED.getMessageDefinition(serverName),
                                    Boolean.toString(permanentDeactivation));
            }

            try
            {
                /*
                 * Shutdown the access services
                 */
                if (instance.getOperationalAccessServiceAdminList() != null)
                {
                    for (AccessServiceAdmin accessServiceAdmin : instance.getOperationalAccessServiceAdminList())
                    {
                        if (accessServiceAdmin != null)
                        {
                            instance.setServerServiceActiveStatus(accessServiceAdmin.getFullServiceName(), ServerActiveStatus.STOPPING);

                            accessServiceAdmin.shutdown();

                            instance.setServerServiceActiveStatus(accessServiceAdmin.getFullServiceName(), ServerActiveStatus.INACTIVE);
                        }
                    }
                }

                /*
                 * Shutdown the view services
                 */
                if (instance.getOperationalViewServiceAdminList() != null)
                {
                    for (ViewServiceAdmin viewServiceAdmin : instance.getOperationalViewServiceAdminList())
                    {
                        if (viewServiceAdmin != null)
                        {
                            instance.setServerServiceActiveStatus(viewServiceAdmin.getFullServiceName(), ServerActiveStatus.STOPPING);

                            viewServiceAdmin.shutdown();

                            instance.setServerServiceActiveStatus(viewServiceAdmin.getFullServiceName(), ServerActiveStatus.INACTIVE);

                        }
                    }
                }
                if (instance.getOperationalViewServerGenericServiceAdminList() != null)
                {
                    for (ViewServerGenericServiceAdmin viewServiceAdmin : instance.getOperationalViewServerGenericServiceAdminList())
                    {
                        if (viewServiceAdmin != null)
                        {
                            instance.setServerServiceActiveStatus(viewServiceAdmin.getFullServiceName(), ServerActiveStatus.STOPPING);

                            viewServiceAdmin.shutdown();

                            instance.setServerServiceActiveStatus(viewServiceAdmin.getFullServiceName(), ServerActiveStatus.INACTIVE);

                        }
                    }
                }

                /*
                 * Shutdown the engine host
                 */
                if (instance.getOperationalEngineHost() != null)
                {
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalEngineHost().terminate();

                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.ENGINE_HOST_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);

                }

                /*
                 * Shutdown the integration daemon
                 */
                if (instance.getOperationalIntegrationDaemon() != null)
                {
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalIntegrationDaemon().terminate();

                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.INTEGRATION_DAEMON_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);
                }

                /*
                 * Shutdown the conformance test suite
                 */
                if (instance.getOperationalConformanceSuiteServices() != null)
                {
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalConformanceSuiteServices().terminate(permanentDeactivation);

                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);
                }

                /*
                 * Terminate the OMRS
                 */
                if (instance.getOperationalRepositoryServices() != null)
                {
                    instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalRepositoryServices().disconnect(permanentDeactivation);

                    instance.setServerServiceActiveStatus(CommonServicesDescription.REPOSITORY_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);
                }

                instance.setServerActiveStatus(ServerActiveStatus.INACTIVE);

                instanceHandler.removeServerServiceInstance(serverName);

                if (auditLog != null)
                {
                    auditLog.logMessage(actionDescription,
                                        ServerOpsAuditCode.SERVER_SHUTDOWN_SUCCESS.getMessageDefinition(serverName),
                                        Boolean.toString(permanentDeactivation));
                }
            }
            catch (Exception   error)
            {
                 if (auditLog != null)
                 {
                     auditLog.logException(actionDescription,
                                           ServerOpsAuditCode.SERVER_SHUTDOWN_ERROR.getMessageDefinition(serverName,
                                                                                                         error.getClass().getName(),
                                                                                                         error.getMessage()),
                                           Boolean.toString(permanentDeactivation),
                                           error);
                 }

                 throw error;
            }

            platformInstanceMap.shutdownServerInstance(userId, serverName, methodName);
        }
    }


    /**
     * Temporarily deactivate the open metadata and governance servers in th supplied list.
     *
     * @param userId  user that is issuing the request
     * @param serverNames list of server names
     */
    public void deactivateTemporarilyServerList(String        userId,
                                                List<String>  serverNames)
    {
        if (serverNames != null)
        {
            for (String serverName : serverNames)
            {
                if (serverName != null)
                {
                    shutdownServer(userId, serverName);
                }
            }
        }
    }


    /**
     * Temporarily deactivate any open metadata and governance services for the requested server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownServer(String  userId,
                                       String  serverName)
    {
        final String methodName = "shutdownServer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                                              serverName,
                                              methodName,
                                              instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                                              false);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse shutdownAndUnregisterServer(String  userId,
                                                    String  serverName)
    {
        final String methodName = "shutdownAndUnregisterServer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);

            deactivateRunningServiceInstances(userId,
                                              serverName,
                                              methodName,
                                              instanceHandler.getServerServiceInstance(userId, serverName, methodName),
                                              true);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (PropertyServerException error)
        {
            exceptionHandler.capturePropertyServerException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /*
     * =============================================================
     * Services on running instances
     */

    /*
     * Query current configuration and status
     */

    /**
     * Return the complete set of configuration properties in use by the server.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or the server is not running.
     */
    public OMAGServerConfigResponse getActiveConfiguration(String userId,
                                                           String serverName)
    {
        final String methodName = "getActiveConfiguration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OMAGServerConfigResponse response = new OMAGServerConfigResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);

            response.setOMAGServerConfig(instance.getOperationalConfiguration());
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Return the status of the server along with it services within.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return OMAGServerConfig properties or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName parameter or the server is not running.
     */
    public OMAGServerStatusResponse getActiveServerStatus(String userId,
                                                          String serverName)
    {
        final String methodName = "getActiveServerStatus";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OMAGServerStatusResponse response = new OMAGServerStatusResponse();

        try
        {
            errorHandler.validateUserId(userId, serverName, methodName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);

            response.setServerStatus(instance.getServerStatus());
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of services that are active on a specific OMAG Server that is active on this OMAG Server Platform.
     *
     * @param userId name of the user making the request
     * @param serverName name of the server of interest
     * @return List of service names
     */
    public ServerServicesListResponse getActiveServices(String    userId,
                                                        String    serverName)
    {
        final String   methodName = "getActiveServiceListForServer";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ServerServicesListResponse response = new ServerServicesListResponse();

        try
        {
            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);

            response.setServerName(serverName);
            response.setServerServicesList(instance.getActiveServiceListForServer());
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param fileName name of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or fileName parameter.
     */
    public VoidResponse addOpenMetadataArchiveFile(String userId,
                                                   String serverName,
                                                   String fileName)
    {
        final String methodName = "addOpenMetadataArchiveFile";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateFileName(fileName, serverName, methodName);

            ConnectorConfigurationFactory configurationFactory   = new ConnectorConfigurationFactory();
            Connection newOpenMetadataArchive = configurationFactory.getOpenMetadataArchiveFileConnection(fileName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);
            OMRSOperationalServices         repositoryServicesInstance = instance.getOperationalRepositoryServices();

            repositoryServicesInstance.addOpenMetadataArchive(serverName, newOpenMetadataArchive, fileName);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param connection connection to access the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or connection parameter.
     */
    public VoidResponse addOpenMetadataArchive(String     userId,
                                               String     serverName,
                                               Connection connection)
    {
        final String methodName = "addOpenMetadataArchive";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validateServerConnection(connection, serverName, methodName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);
            OMRSOperationalServices         repositoryServicesInstance = instance.getOperationalRepositoryServices();

            repositoryServicesInstance.addOpenMetadataArchive(serverName, connection, methodName);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Add a new open metadata archive to running repository.
     *
     * @param userId  user that is issuing the request.
     * @param serverName  local server name.
     * @param openMetadataArchive contents of the open metadata archive file.
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException invalid serverName or openMetadataArchive parameter.
     */
    public VoidResponse addOpenMetadataArchive(String              userId,
                                               String              serverName,
                                               OpenMetadataArchive openMetadataArchive)
    {
        final String methodName = "addOpenMetadataArchive";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();

        try
        {
            errorHandler.validateServerName(serverName, methodName);
            errorHandler.validateUserId(userId, serverName, methodName);
            errorHandler.validatePropertyNotNull(openMetadataArchive, "openMetadataArchive", serverName, methodName);

            OMAGOperationalServicesInstance instance = instanceHandler.getServerServiceInstance(userId, serverName, methodName);
            OMRSOperationalServices         repositoryServicesInstance = instance.getOperationalRepositoryServices();
            OpenMetadataArchiveWrapper      archiveWrapper = new OpenMetadataArchiveWrapper();

            archiveWrapper.setArchiveContents(openMetadataArchive);

            repositoryServicesInstance.addOpenMetadataArchive(serverName, archiveWrapper, methodName);
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
        }
        catch (Exception error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
        }
        catch (Throwable error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, methodName, null);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
