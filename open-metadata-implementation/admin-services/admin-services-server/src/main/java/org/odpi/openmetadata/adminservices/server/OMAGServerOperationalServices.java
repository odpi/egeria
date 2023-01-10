/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.server;


import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode;
import org.odpi.openmetadata.adminservices.classifier.ServerTypeClassifier;
import org.odpi.openmetadata.adminservices.configuration.properties.*;
import org.odpi.openmetadata.adminservices.configuration.registration.*;
import org.odpi.openmetadata.adminservices.ffdc.OMAGAdminErrorCode;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGConfigurationErrorException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException;
import org.odpi.openmetadata.adminservices.ffdc.exception.OMAGNotAuthorizedException;
import org.odpi.openmetadata.adminservices.properties.ServerActiveStatus;
import org.odpi.openmetadata.adminservices.registration.AccessServiceAdmin;
import org.odpi.openmetadata.adminservices.registration.ViewServiceAdmin;
import org.odpi.openmetadata.adminservices.rest.OMAGServerConfigResponse;
import org.odpi.openmetadata.adminservices.rest.OMAGServerStatusResponse;
import org.odpi.openmetadata.adminservices.rest.SuccessMessageResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.gaf.admin.GAFMetadataOperationalServices;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.admin.OCFMetadataOperationalServices;
import org.odpi.openmetadata.conformance.server.ConformanceSuiteOperationalServices;
import org.odpi.openmetadata.governanceservers.enginehostservices.server.EngineHostOperationalServices;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.server.IntegrationDaemonOperationalServices;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.governanceservers.dataengineproxy.admin.DataEngineProxyOperationalServices;
import org.odpi.openmetadata.governanceservers.openlineage.admin.OpenLineageServerOperationalServices;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMAGServerOperationalServices will provide support to start, manage and stop services in the OMAG Server.
 */
public class OMAGServerOperationalServices
{
    private final OMAGServerOperationalInstanceHandler instanceHandler = new OMAGServerOperationalInstanceHandler(CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    private final OMAGServerPlatformInstanceMap        platformInstanceMap = new OMAGServerPlatformInstanceMap();

    private final OMAGServerAdminStoreServices   configStore  = new OMAGServerAdminStoreServices();
    private final OMAGServerErrorHandler         errorHandler = new OMAGServerErrorHandler();
    private final OMAGServerExceptionHandler     exceptionHandler = new OMAGServerExceptionHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMAGServerOperationalServices.class),
                                                                            CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName());

    /*
     * =============================================================
     * Initialization and shutdown
     */

    /**
     * Activate the list of open metadata and governance servers using the stored configuration information.
     * The code works through the list, starting each server in turn.  It stops if one of the servers fails to
     * start and returns the error.  Otherwise it continues through the list, returning the successful
     * start up messages.
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
            response.setSuccessMessage(new Date().toString() + " " + startUpMessage);
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

            response = activateWithSuppliedConfig(userId, serverName, configStore.getServerConfig(userId, serverName, false, methodName));
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

        return response;
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

            /*
             * If the server type is not set then use the value from the classification.
             */
            if (configuration.getLocalServerType() == null)
            {
                configuration.setLocalServerType(serverTypeClassification.getServerTypeName());
            }

            /*
             * Save the configuration document to the config store.  This ensures we have the latest version of the
             * config document on file.
             */
            configStore.saveServerConfig(serverName, methodName, configuration);

            /*
             * Validate that the server is not running already.  If it is running it should be shutdown.
             */
            if (instanceHandler.isServerActive(userId, serverName))
            {
                this.deactivateTemporarily(userId, serverName);
            }

            /*
             * The instance saves the operational services objects for this server instance so they can be retrieved
             * in response to subsequent REST calls for the server.  These instances provide the multi-tenant
             * support in Egeria.
             */
            instance = new OMAGOperationalServicesInstance(serverName,
                                                           serverTypeClassification,
                                                           CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName(),
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
             * Each subsystem should be logging the start up of their components and handling
             * their errors.  However the logging and error handling done by this method is to bracket the
             * start up of the different types of subsystems and provide minimal diagnostics for
             * immature subsystems that have not yet developed their logging and error handling.
             */
            OMRSAuditLog auditLog = operationalRepositoryServices.getAuditLog(
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceCode(),
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceDevelopmentStatus(),
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceName(),
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceDescription(),
                    CommonServicesDescription.ADMIN_OPERATIONAL_SERVICES.getServiceWiki());
            instance.setAuditLog(auditLog);

            /*
             * There are many paging services in Egeria.  This value sets a maximum page size that a requester can use.
             * It is passed to each subsystem at start up so it can enforce the limit on all paging REST calls.
             * Having a limit helps to prevent a denial of service attack that uses very large requests to overwhelm the server.
             * If this value is 0 it means there is no upper limit.  If this value is negative then it is invalid.
             */
            this.validateMaxPageSize(configuration.getMaxPageSize(), serverName, auditLog, methodName);

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
            if ((ServerTypeClassification.METADATA_SERVER.equals(serverTypeClassification)) ||
                (ServerTypeClassification.METADATA_ACCESS_POINT.equals(serverTypeClassification)) ||
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
                 * Next initialize the Open Connector Framework (OCF) metadata services.  These services are only initialized
                 * if the enterprise repository services are enabled.  They support requests for metadata from connectors running
                 * outside the metadata server.
                 */
                OMRSRepositoryConnector enterpriseRepositoryConnector
                        = operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName());

                if (enterpriseRepositoryConnector != null)
                {
                    /*
                     * The enterprise repository services have been requested so OCF metadata management can be started.
                     */
                    OCFMetadataOperationalServices operationalOCFMetadataServices;

                    instance.setServerServiceActiveStatus(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.STARTING);
                    operationalOCFMetadataServices = new OCFMetadataOperationalServices(configuration.getLocalServerName(),
                                                                                        enterpriseRepositoryConnector,
                                                                                        operationalRepositoryServices.getAuditLog(
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceCode(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceDevelopmentStatus(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceDescription(),
                                                                                                CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceWiki()),
                                                                                        configuration.getLocalServerUserId(),
                                                                                        configuration.getMaxPageSize());

                    instance.setOperationalOCFMetadataServices(operationalOCFMetadataServices);
                    activatedServiceList.add(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName());

                    /*
                     * The enterprise repository services have been requested so GAF metadata management can also be started.
                     */
                    GAFMetadataOperationalServices operationalGAFMetadataServices;

                    instance.setServerServiceActiveStatus(CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.STARTING);
                    operationalGAFMetadataServices = new GAFMetadataOperationalServices(configuration.getLocalServerName(),
                                                                                        enterpriseRepositoryConnector,
                                                                                        operationalRepositoryServices.getAuditLog(
                                                                                                CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceCode(),
                                                                                                CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceDevelopmentStatus(),
                                                                                                CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName(),
                                                                                                CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceDescription(),
                                                                                                CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceWiki()),
                                                                                        configuration.getLocalServerUserId(),
                                                                                        configuration.getMaxPageSize());

                    instance.setOperationalGAFMetadataServices(operationalGAFMetadataServices);
                    activatedServiceList.add(CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName());
                }

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
                OMRSTopicConnector enterpriseTopicConnector = operationalRepositoryServices.getEnterpriseOMRSTopicConnector();

                initializeAccessServices(instance,
                                         configuration.getAccessServicesConfig(),
                                         operationalRepositoryServices,
                                         enterpriseTopicConnector,
                                         configuration.getLocalServerUserId(),
                                         serverName,
                                         activatedServiceList,
                                         auditLog);

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
                                                                   enterpriseTopicConnector,
                                                                   operationalRepositoryServices.getEnterpriseConnectorManager(),
                                                                   operationalRepositoryServices.getAuditLog(
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceCode(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceDevelopmentStatus(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceDescription(),
                                                                           GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceWiki()));

                    activatedServiceList.add(GovernanceServicesDescription.CONFORMANCE_SUITE_SERVICES.getServiceName());
                }

                /*
                 * The enterprise topic passes OMRS Events from the cohort to the listening access services.
                 * During the access services start up, they registered listeners with the enterprise topic.
                 * Starting the enterprise topic will start the flow of events to the registered access services.
                 */
                if (enterpriseTopicConnector != null)
                {
                    try
                    {
                        enterpriseTopicConnector.start();
                    }
                    catch (Exception  error)
                    {
                        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.ENTERPRISE_TOPIC_START_FAILED.getMessageDefinition(serverName,
                                                                                                                                        "in memory",
                                                                                                                                        error.getClass().getName(),
                                                                                                                                        error.getMessage()),
                                                                  this.getClass().getName(),
                                                                  methodName);
                    }
                }
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
                 * However they may also host specific types of engines, or provide an implementation of a complete governance service.
                 * Because of this variety, Egeria does not (yet) provide any specialist frameworks for supporting the governance servers.
                 * all the implementation is in the governance services subsystems initialized below.
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
                                        OMAGAdminAuditCode.STARTING_GOVERNANCE_SERVICES.getMessageDefinition(serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                             serverName));

                    initializeGovernanceServices(instance,
                                                 configuration,
                                                 serverTypeClassification,
                                                 operationalRepositoryServices,
                                                 activatedServiceList);

                    auditLog.logMessage(actionDescription,
                                        OMAGAdminAuditCode.GOVERNANCE_SERVICES_STARTED.getMessageDefinition(serverTypeClassifier.getServerType().getServerTypeName(),
                                                                                                            serverName));
                }
                catch (OMAGConfigurationErrorException  error)
                {
                    /*
                     * There is a configuration error that means that the governance services subsystem can not start.  Since this is
                     * the primary function of the server then there is no purpose in continuing.
                     */
                    auditLog.logException(actionDescription,
                                          OMAGAdminAuditCode.GOVERNANCE_SERVICE_FAILURE.getMessageDefinition(error.getClass().getName(),
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
                                          OMAGAdminAuditCode.GOVERNANCE_SERVICE_FAILURE.getMessageDefinition(error.getClass().getName(),
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

            String successMessage = new Date().toString() + " " + serverName + " is running the following services: " + activatedServiceList.toString();

            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.SERVER_STARTUP_SUCCESS.getMessageDefinition(serverName,
                                                                                               activatedServiceList.toString()));

            response.setSuccessMessage(successMessage);
        }
        catch (UserNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, methodName, instance);
        }
        catch (OMAGConfigurationErrorException  error)
        {
            exceptionHandler.captureConfigurationErrorException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, methodName, instance);
        }
        catch (OMAGInvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, methodName, instance);
        }
        catch (OMAGNotAuthorizedException error)
        {
            exceptionHandler.captureNotAuthorizedException(response, error);
            cleanUpRunningServiceInstances(userId, serverName, methodName, instance);
        }
        catch (Exception  error)
        {
            exceptionHandler.capturePlatformRuntimeException(serverName, methodName, response, error);
            cleanUpRunningServiceInstances(userId, serverName, methodName, instance);
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
     * @param methodName calling method
     * @throws OMAGConfigurationErrorException the max page size is negative.
     */
    private void validateMaxPageSize(int          maxPageSize,
                                     String       serverName,
                                     OMRSAuditLog auditLog,
                                     String       methodName) throws OMAGConfigurationErrorException
    {
        final String actionDescription = "Validating max page size during server initialization";

        if (maxPageSize > 0)
        {
            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.MAX_PAGE_SIZE.getMessageDefinition(serverName, Integer.toString(maxPageSize)));
        }
        else if (maxPageSize == 0)
        {
            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.UNLIMITED_MAX_PAGE_SIZE.getMessageDefinition(serverName));
        }
        else
        {
            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.INVALID_MAX_PAGE_SIZE.getMessageDefinition(serverName,
                                                                                              Integer.toString(maxPageSize)));

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_MAX_PAGE_SIZE.getMessageDefinition(serverName,
                                                                                                                Integer.toString(maxPageSize)),
                                                      this.getClass().getName(),
                                                      methodName);
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
     * @param serverName server name
     * @param activatedServiceList list of services (subsystems) running in the server
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    @SuppressWarnings(value = "deprecation")
    private void initializeAccessServices(OMAGOperationalServicesInstance instance,
                                          List<AccessServiceConfig>       accessServiceConfigList,
                                          OMRSOperationalServices         operationalRepositoryServices,
                                          OMRSTopicConnector              enterpriseTopicConnector,
                                          String                          localServerUserId,
                                          String                          serverName,
                                          List<String>                    activatedServiceList,
                                          OMRSAuditLog                    auditLog) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeAccessServices";
        final String actionDescription = "Initialize Access Services";

        List<AccessServiceAdmin> operationalAccessServiceAdminList = instance.getOperationalAccessServiceAdminList();
        if (accessServiceConfigList != null)
        {
            auditLog.logMessage(actionDescription, OMAGAdminAuditCode.STARTING_ACCESS_SERVICES.getMessageDefinition());

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
                        instance.setServerServiceActiveStatus(accessServiceConfig.getAccessServiceFullName(), ServerActiveStatus.STARTING);

                        try
                        {
                            AccessServiceAdmin accessServiceAdmin = this.getAccessServiceAdminClass(accessServiceConfig, auditLog, serverName);

                            accessServiceAdmin.setFullServiceName(accessServiceConfig.getAccessServiceFullName());


                            /*
                             * Each access service has its own audit log instance.
                             */
                            OMRSAuditLog accessServicesAuditLog
                                    = operationalRepositoryServices.getAuditLog(accessServiceConfig.getAccessServiceId(),
                                                                                accessServiceConfig.getAccessServiceDevelopmentStatus(),
                                                                                accessServiceConfig.getAccessServiceFullName(),
                                                                                accessServiceConfig.getAccessServiceDescription(),
                                                                                accessServiceConfig.getAccessServiceWiki());

                            /*
                             * We will switch to the new version of this method once all access services have move from using OMRSAuditLog to
                             * AuditLog.  The default implementation of this method delegates to the new version of the method so
                             */
                            accessServiceAdmin.initialize(accessServiceConfig,
                                                          enterpriseTopicConnector,
                                                          operationalRepositoryServices.getEnterpriseOMRSRepositoryConnector(accessServiceConfig.getAccessServiceFullName()),
                                                          accessServicesAuditLog,
                                                          localServerUserId);
                            operationalAccessServiceAdminList.add(accessServiceAdmin);
                            activatedServiceList.add(accessServiceConfig.getAccessServiceFullName());
                            instance.setServerServiceActiveStatus(accessServiceAdmin.getFullServiceName(), ServerActiveStatus.RUNNING);
                        }
                        catch (OMAGConfigurationErrorException error)
                        {
                            auditLog.logException(methodName,
                                                  OMAGAdminAuditCode.ACCESS_SERVICE_INSTANCE_FAILURE.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                                          error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  error);
                            throw error;
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(methodName,
                                                  OMAGAdminAuditCode.ACCESS_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                          accessServiceConfig.getAccessServiceName(),
                                                                                                                          error.getMessage()),
                                                  accessServiceConfig.toString(),
                                                  error);

                            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(serverName,
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
                                            OMAGAdminAuditCode.SKIPPING_ACCESS_SERVICE.getMessageDefinition(accessServiceConfig.getAccessServiceFullName(),
                                                                                                            serverName));
                    }
                }
            }

            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.ALL_ACCESS_SERVICES_STARTED.getMessageDefinition(Integer.toString(enabledAccessServiceCount),
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
     * @param activatedServiceList          list of services (subsystems) running in the server
     * @param maxPageSize                   maximum page size. 0 means unlimited
     * @param auditLog                      audit log
     * @throws OMAGConfigurationErrorException problem with the configuration
     */
    private void initializeViewServices(OMAGOperationalServicesInstance instance,
                                        List<ViewServiceConfig>         viewServiceConfigList,
                                        OMRSOperationalServices         operationalRepositoryServices,
                                        String                          localServerUserId,
                                        String                          serverName,
                                        List<String>                    activatedServiceList,
                                        int                             maxPageSize,
                                        OMRSAuditLog                    auditLog) throws OMAGConfigurationErrorException
    {
        final String methodName = "initializeViewServices";
        final String actionDescription = "Initialize View Services";

        List<ViewServiceAdmin> operationalViewServiceAdminList = instance.getOperationalViewServiceAdminList();
        if (viewServiceConfigList != null)
        {
            auditLog.logMessage(actionDescription, OMAGAdminAuditCode.STARTING_VIEW_SERVICES.getMessageDefinition());

            /*
             * Need to count the view services because of the possibility of deprecated or disabled view services in the list.
             */
            int configuredViewServiceCount = 0;
            int enabledViewServiceCount = 0;

            for (ViewServiceConfig viewServiceConfig : viewServiceConfigList)
            {
                configuredViewServiceCount++;

                if (ServiceOperationalStatus.ENABLED.equals(viewServiceConfig.getViewServiceOperationalStatus()))
                {
                    enabledViewServiceCount++;
                    instance.setServerServiceActiveStatus(viewServiceConfig.getViewServiceFullName(), ServerActiveStatus.STARTING);

                    try
                    {
                        ViewServiceAdmin viewServiceAdmin = this.getViewServiceAdminClass(viewServiceConfig, auditLog, serverName);

                        /*
                         * Each view service has its own audit log instance.
                         */
                        OMRSAuditLog viewServicesAuditLog
                                = operationalRepositoryServices.getAuditLog(viewServiceConfig.getViewServiceId(),
                                                                            viewServiceConfig.getViewServiceDevelopmentStatus(),
                                                                            viewServiceConfig.getViewServiceFullName(),
                                                                            viewServiceConfig.getViewServiceDescription(),
                                                                            viewServiceConfig.getViewServiceWiki());

                        viewServiceAdmin.initialize(serverName,
                                                    viewServiceConfig,
                                                    viewServicesAuditLog,
                                                    localServerUserId,
                                                    maxPageSize);
                        operationalViewServiceAdminList.add(viewServiceAdmin);
                        activatedServiceList.add(viewServiceConfig.getViewServiceFullName());
                        instance.setServerServiceActiveStatus(viewServiceConfig.getViewServiceFullName(), ServerActiveStatus.RUNNING);
                    }
                    catch (OMAGConfigurationErrorException error)
                    {
                        auditLog.logException(methodName,
                                              OMAGAdminAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(viewServiceConfig.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              viewServiceConfig.toString(),
                                              error);
                        throw error;
                    }
                    catch (Exception error)
                    {
                        auditLog.logException(methodName,
                                              OMAGAdminAuditCode.VIEW_SERVICE_INSTANCE_FAILURE.getMessageDefinition(error.getMessage(),
                                                                                                                    viewServiceConfig.getViewServiceName(),
                                                                                                                    error.getMessage()),
                                              viewServiceConfig.toString(),
                                              error);

                        throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION.getMessageDefinition(serverName,
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
                                        OMAGAdminAuditCode.SKIPPING_VIEW_SERVICE.getMessageDefinition(viewServiceConfig.getViewServiceFullName(),
                                                                                                      serverName));
                }

            }

            auditLog.logMessage(actionDescription,
                                OMAGAdminAuditCode.ALL_VIEW_SERVICES_STARTED.getMessageDefinition(Integer.toString(enabledViewServiceCount),
                                                                                                  Integer.toString(configuredViewServiceCount)));
        }

        /*
         * Save the list of running view services to the instance and then add the instance to the instance map.
         * The instance information can then be retrieved for shutdown or other management requests.
         */
        instance.setOperationalViewServiceAdminList(operationalViewServiceAdminList);
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
                                      OMAGAdminAuditCode.BAD_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(accessServiceConfig.getAccessServiceName(),
                                                                                                             accessServiceAdminClassName,
                                                                                                             error.getMessage()),
                                      accessServiceConfig.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
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
                                OMAGAdminAuditCode.NULL_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                        accessServiceConfig.getAccessServiceFullName()),
                                accessServiceConfig.toString());

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_ACCESS_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                              accessServiceConfig.getAccessServiceName()),
                                                      this.getClass().getName(),
                                                      methodName);
        }
    }


    /**
     * Get the View Service admin class for a named server's view service configuration.
     *
     * @param viewServiceConfig configuration for the view service
     * @param auditLog logging destination
     * @param serverName this server instance
     * @return Admin class for the view service
     * @throws OMAGConfigurationErrorException if the class is invalid
     */
    private ViewServiceAdmin getViewServiceAdminClass(ViewServiceConfig     viewServiceConfig,
                                                      OMRSAuditLog          auditLog,
                                                      String                serverName) throws OMAGConfigurationErrorException
    {
        final String methodName = "getViewServiceAdminClass";

        String    viewServiceAdminClassName = viewServiceConfig.getViewServiceAdminClass();

        if (viewServiceAdminClassName != null)
        {
            try
            {
                return (ViewServiceAdmin) Class.forName(viewServiceAdminClassName).getDeclaredConstructor().newInstance();
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      OMAGAdminAuditCode.BAD_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(viewServiceConfig.getViewServiceName(),
                                                                                                           viewServiceAdminClassName,
                                                                                                           error.getMessage()),
                                      viewServiceConfig.toString(),
                                      error);

                throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.BAD_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                               viewServiceAdminClassName,
                                                                                                                               viewServiceConfig.getViewServiceName()),
                                                          this.getClass().getName(),
                                                          methodName,
                                                          error);
            }
        }
        else
        {
            auditLog.logMessage(methodName,
                                OMAGAdminAuditCode.NULL_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                      viewServiceConfig.getViewServiceFullName()),
                                viewServiceConfig.toString());

            throw new OMAGConfigurationErrorException(OMAGAdminErrorCode.NULL_VIEW_SERVICE_ADMIN_CLASS.getMessageDefinition(serverName,
                                                                                                                            viewServiceConfig.getViewServiceName()),
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
         * Initialize the Data Engine Proxy Services.  This is a governance server that extracts metadata about processes from
         * a data engine.
         */
        if (ServerTypeClassification.DATA_ENGINE_PROXY.equals(serverTypeClassification))
        {
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(), ServerActiveStatus.STARTING);

            DataEngineProxyOperationalServices operationalDataEngineProxyServices
                    = new DataEngineProxyOperationalServices(configuration.getLocalServerName(),
                                                             configuration.getLocalServerId(),
                                                             configuration.getLocalServerUserId(),
                                                             configuration.getLocalServerPassword());

            instance.setOperationalDataEngineProxyServices(operationalDataEngineProxyServices);
            operationalDataEngineProxyServices.initialize(configuration.getDataEngineProxyConfig(),
                                                          operationalRepositoryServices.getAuditLog(
                                                                  GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceCode(),
                                                                  GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceDevelopmentStatus(),
                                                                  GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(),
                                                                  GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceDescription(),
                                                                  GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceWiki()));

            activatedServiceList.add(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName());
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);
        }

        /*
         * Initialize the Engine Host Services for the Engine Host OMAG server.  This is a governance server for running governance engines.
         */
        else if (ServerTypeClassification.ENGINE_HOST.equals(serverTypeClassification))
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
                                                               configuration.getLocalServerUserId(),
                                                               configuration.getLocalServerPassword(),
                                                               configuration.getMaxPageSize());

            instance.setOperationalIntegrationDaemon(integrationDaemonOperationalServices);
            List<String> integrationServices = integrationDaemonOperationalServices.initialize(configuration.getIntegrationServicesConfig(),
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

        /*
         * Initialize the Open Lineage Services.  This is supports the storing and querying of asset lineage.
         */
        else if (ServerTypeClassification.OPEN_LINEAGE_SERVER.equals(serverTypeClassification))
        {
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(), ServerActiveStatus.STARTING);

            OpenLineageServerOperationalServices
                    operationalOpenLineageServer = new OpenLineageServerOperationalServices(configuration.getLocalServerId(),
                                                                                            configuration.getLocalServerName(),
                                                                                            configuration.getLocalServerUserId(),
                                                                                            configuration.getLocalServerPassword(),
                                                                                            configuration.getMaxPageSize());
            instance.setOpenLineageOperationalServices(operationalOpenLineageServer);
            operationalOpenLineageServer.initialize(configuration.getOpenLineageServerConfig(),
                                                    operationalRepositoryServices.getAuditLog(
                                                            GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceCode(),
                                                            GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceDevelopmentStatus(),
                                                            GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(),
                                                            GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceDescription(),
                                                            GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceWiki()));

            activatedServiceList.add(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName());
            instance.setServerServiceActiveStatus(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(), ServerActiveStatus.RUNNING);
        }
    }


    /**
     * Called when server start up fails.  The aim is to clean up any partially running services.
     * Any exceptions are ignored as the real cause of the error is already captured.
     *
     * @param userId calling user
     * @param serverName name of this server
     * @param methodName calling method
     * @param instance a list of the running services
     */
    private void cleanUpRunningServiceInstances(String                          userId,
                                                String                          serverName,
                                                String                          methodName,
                                                OMAGOperationalServicesInstance instance)
    {
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
    private void deactivateRunningServiceInstances(String                          userId,
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
                                    OMAGAdminAuditCode.SERVER_SHUTDOWN_STARTED.getMessageDefinition(serverName),
                                    Boolean.toString(permanentDeactivation));
            }

            try
            {
                /*
                 * Shutdown the data engine proxy services
                 */
                if (instance.getOperationalDataEngineProxyServices() != null)
                {
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalDataEngineProxyServices().disconnect();

                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.DATA_ENGINE_PROXY_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);
                }

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

                /*
                 * Shutdown the OCF metadata management services
                 */
                if (instance.getOperationalOCFMetadataServices() != null)
                {
                    instance.setServerServiceActiveStatus(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalOCFMetadataServices().shutdown();

                    instance.setServerServiceActiveStatus(CommonServicesDescription.OCF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.INACTIVE);
                }

                /*
                 * Shutdown the GAF metadata management services
                 */
                if (instance.getOperationalGAFMetadataServices() != null)
                {
                    instance.setServerServiceActiveStatus(CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOperationalGAFMetadataServices().shutdown();

                    instance.setServerServiceActiveStatus(CommonServicesDescription.GAF_METADATA_MANAGEMENT.getServiceName(), ServerActiveStatus.INACTIVE);
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
                 * Shutdown the open lineage services
                 */
                if (instance.getOpenLineageOperationalServices() != null)
                {
                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(), ServerActiveStatus.STOPPING);

                    instance.getOpenLineageOperationalServices().shutdown();

                    instance.setServerServiceActiveStatus(GovernanceServicesDescription.OPEN_LINEAGE_SERVICES.getServiceName(), ServerActiveStatus.INACTIVE);
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
                                        OMAGAdminAuditCode.SERVER_SHUTDOWN_SUCCESS.getMessageDefinition(serverName),
                                        Boolean.toString(permanentDeactivation));
                }
            }
            catch (Exception   error)
            {
                 if (auditLog != null)
                 {
                     auditLog.logException(actionDescription,
                                           OMAGAdminAuditCode.SERVER_SHUTDOWN_ERROR.getMessageDefinition(serverName,
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
                    deactivateTemporarily(userId, serverName);
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
    public VoidResponse deactivateTemporarily(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivateTemporarily";

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

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Terminate any running open metadata and governance services, remove the server from any open metadata cohorts
     * and delete the server's configuration.
     *
     * @param userId  user that is issuing the request
     * @param serverName  local server name
     * @return void response or
     * OMAGNotAuthorizedException the supplied userId is not authorized to issue this command or
     * OMAGInvalidParameterException the serverName is invalid.
     */
    public VoidResponse deactivatePermanently(String  userId,
                                              String  serverName)
    {
        final String methodName = "deactivatePermanently";

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

            /*
             * Delete the configuration for this server
             */
            configStore.saveServerConfig(serverName, methodName, null);
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
     * OMAGInvalidParameterException invalid serverName parameter or the server is not runing.
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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Return the status of the server and it services within.
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

            repositoryServicesInstance.addOpenMetadataArchive(newOpenMetadataArchive, fileName);
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

            repositoryServicesInstance.addOpenMetadataArchive(connection, methodName);
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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
